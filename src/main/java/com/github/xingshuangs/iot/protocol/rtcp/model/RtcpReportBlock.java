/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

/**
 * Report block.
 * (数据块)
 *
 * @author xingshuang
 */
@Data
public class RtcpReportBlock implements IObjectByteArray {

    /**
     * Source id.
     * (接收端的源（32bit）：接收端的源)
     */
    private long sourceId;

    /**
     * Fraction lost.
     * 丢包率（8bit）:丢包率需要转换为0-255的占比；如20%丢包=20%*255=51
     * 表明从上一个SR或RR包发出以来从同步源n(SSRC_n)来的RTP数据包的丢失率
     */
    private int fractionLost;

    /**
     * Cumulative packet lost.
     * 累计丢包数（24bit）：从开始计算，丢包的数量统计，当重传后需要从丢包数中移除；
     * 从开始接收到SSRC_n的包到发送SR,从SSRC_n传过来的RTP数据包的丢失总数。
     */
    private int cumulativePacketLost;

    /**
     * Expert highest sequence number received.
     * 期望序列号（32bit）：期望接收的最大序列号，低16位存储期望最大序列号；高16为翻转次数统计；
     * 从SSRC_n收到的RTP数据包中最大的序列号，
     */
    private long extHighestSequenceNumberReceived;

    /**
     * Jitter.
     * 到达时间抖动（32bit）：到达时间间隔的统计方差；
     * RTP数据包接受时间的统计方差估计
     */
    private long jitter;

    /**
     * Last Ntp time sender report received.
     * 最后一次发送SR时间（32bit）:LSR最后一次发送SR的时间；
     * 取最近从SSRC_n收到的SR包中的NTP时间戳的中间32比特。如果目前还没收到SR包，则该域清零。
     */
    private long lastNtpTimeSenderReportReceived;

    /**
     * Delay since last time sender report received.
     * 最后一次接收SR到发送的时延（32bit）:DLSR最后一次收到SR包后到发送中间的时延；
     * 上次从SSRC_n收到SR包到发送本报告的延时。
     */
    private long delaySinceLastTimeSenderReportReceived;

    @Override
    public int byteArrayLength() {
        return 24;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(24)
                .putInteger(this.sourceId)
                .putByte(this.fractionLost)
                .putBytes(IntegerUtil.toCustomByteArray(this.cumulativePacketLost, 1, 3))
                .putInteger(this.extHighestSequenceNumberReceived)
                .putInteger(this.jitter)
                .putInteger(this.lastNtpTimeSenderReportReceived)
                .putInteger(this.delaySinceLastTimeSenderReportReceived)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static RtcpReportBlock fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static RtcpReportBlock fromBytes(final byte[] data, final int offset) {
        if (data.length < 24) {
            throw new IndexOutOfBoundsException("RtcpSdesChunk, data length < 24");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpReportBlock res = new RtcpReportBlock();
        res.sourceId = buff.getUInt32();
        res.fractionLost = buff.getByteToInt();
        res.cumulativePacketLost = IntegerUtil.toInt32In3Bytes(buff.getBytes(3), 0);
        res.extHighestSequenceNumberReceived = buff.getUInt32();
        res.jitter = buff.getUInt32();
        res.lastNtpTimeSenderReportReceived = buff.getUInt32();
        res.delaySinceLastTimeSenderReportReceived = buff.getUInt32();
        return res;
    }
}
