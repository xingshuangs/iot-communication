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

package com.github.xingshuangs.iot.protocol.rtsp.model.interleaved;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RTSP interleaved.
 * 交错帧
 *
 * @author xingshuang
 */
@Data
public class RtspInterleaved implements IObjectByteArray {

    public static final byte VERSION = (byte) 0x24;

    /**
     * Fixed header.
     * 固定头，1个字节，表示Interleave Frame层的开始
     */
    private byte dollarSign = VERSION;

    /**
     * Channel id.
     * 通道Id，1个字节，协议类型，一般 0：Video RTP，1：Video RTCP，2: Audio RTP，3：Audio RTCP
     */
    private int channelId = 0;

    /**
     * Length.
     * 数据长度，2个字节, RTP包的大小
     */
    private int length = 0;

    /**
     * Payload.
     * 剩下的就是负载
     */
    private byte[] payload = new byte[0];

    @Override
    public int byteArrayLength() {
        return 4 + payload.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.dollarSign)
                .putByte(this.channelId)
                .putInteger(this.length)
                .putBytes(this.payload)
                .getData();
    }

    public static List<RtspInterleaved> listFromBytes(final byte[] data) {
        List<RtspInterleaved> res = new ArrayList<>();
        int offset = 0;
        while (offset < data.length) {
            RtspInterleaved rtspInterleaved = fromBytes(data, offset);
            res.add(rtspInterleaved);
            offset += rtspInterleaved.byteArrayLength();
        }
        return res;
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static RtspInterleaved fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static RtspInterleaved fromBytes(final byte[] data, final int offset) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("RtspInterleaved, data length < 4");
        }
        if (data[offset] != VERSION) {
            // 报文版本不一致，第一个字节不是0x24
            throw new IllegalArgumentException("The versions of the packets are different, the first byte is not 0x24");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtspInterleaved res = new RtspInterleaved();
        res.dollarSign = buff.getByte();
        res.channelId = buff.getByteToInt();
        res.length = buff.getUInt16();
        res.payload = buff.getBytes(res.length);
        return res;
    }
}
