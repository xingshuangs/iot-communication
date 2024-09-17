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


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;

/**
 * App
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |V=2|P| subtype |   PT=APP=204  |             length            |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                           SSRC/CSRC                           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                          name (ASCII)                         |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                   application-dependent data                ...
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class RtcpApp extends RtcpBasePackage {

    /**
     * 同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。
     */
    private long sourceId;

    /**
     * 名称
     */
    private String name;

    @Override
    public int byteArrayLength() {
        return 12;
    }

    @Override
    public byte[] toByteArray() {
        byte[] des = new byte[4];
        byte[] src = name.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(src, 0, des, 0, Math.min(des.length, src.length));
        return ByteWriteBuff.newInstance(12)
                .putBytes(this.header.toByteArray())
                .putInteger(this.sourceId)
                .putBytes(des)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static RtcpApp fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static RtcpApp fromBytes(final byte[] data, final int offset) {
        if (data.length < 12) {
            throw new IndexOutOfBoundsException("RtcpBye, data length < 12");
        }
        RtcpApp res = new RtcpApp();
        res.header = RtcpHeader.fromBytes(data, offset);

        ByteReadBuff buff = new ByteReadBuff(data, offset + res.header.byteArrayLength());
        res.sourceId = buff.getUInt32();
        res.name = buff.getString(4);
        return res;
    }
}
