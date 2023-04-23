package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

/**
 * 数据块
 *
 * @author xingshuang
 */
@Data
public class RtcpReportBlock implements IObjectByteArray {

    /**
     * 接收端的源（32bit）：接收端的源
     */
    private long sourceId;

    /**
     * 丢包率（8bit）:丢包率需要转换为0-255的占比；如20%丢包=20%*255=51
     */
    private int fractionLost;

    /**
     * 累计丢包数（24bit）：从开始计算，丢包的数量统计，当重传后需要从丢包数中移除；
     */
    private int cumulativePacketLost;

    /**
     * 期望序列号（32bit）：期望接收的最大序列号，低16位存储期望最大序列号；高16为翻转次数统计；
     */
    private long extHighestSequenceNumberReceived;

    /**
     * 到达时间抖动（32bit）：到达时间间隔的统计方差；
     */
    private long jitter;

    /**
     * 最后一次发送SR时间（32bit）:LSR最后一次发送SR的时间；
     */
    private long lastNtpTimeSenderReportReceived;

    /**
     * 最后一次接收SR到发送的时延（32bit）:DLSR最后一次收到SR包后到发送中间的时延；
     */
    private long delaySinceLastTimeSenderReportReceived;

    @Override
    public int byteArrayLength() {
        return 24;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(4)
                .putInteger(this.sourceId)
                .putByte(this.fractionLost)
                .putBytes(IntegerUtil.toCustomByteArray(this.cumulativePacketLost,0,3))
                .putInteger(this.extHighestSequenceNumberReceived)
                .putInteger(this.jitter)
                .putInteger(this.lastNtpTimeSenderReportReceived)
                .putInteger(this.delaySinceLastTimeSenderReportReceived)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpReportBlock fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpReportBlock fromBytes(final byte[] data, final int offset) {
        if (data.length < 24) {
            throw new IndexOutOfBoundsException("解析RtcpSdesChunk时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpReportBlock res = new RtcpReportBlock();
        res.sourceId = buff.getUInt32();
        res.fractionLost = buff.getByteToInt();
        res.cumulativePacketLost = IntegerUtil.toInt32In3Bytes(buff.getBytes(3),0);
        res.extHighestSequenceNumberReceived = buff.getUInt32();
        res.jitter = buff.getUInt32();
        res.lastNtpTimeSenderReportReceived = buff.getUInt32();
        res.delaySinceLastTimeSenderReportReceived = buff.getUInt32();
        return res;
    }
}
