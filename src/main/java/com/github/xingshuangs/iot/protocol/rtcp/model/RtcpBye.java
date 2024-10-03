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
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpPackageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * bye
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |V=2|P|    SC   |   PT=BYE=203  |             length            |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                           SSRC/CSRC                           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * :                              ...                              :
 * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
 * |     length    |               reason for leaving            ...
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class RtcpBye extends RtcpBasePackage {

    /**
     * Source id.
     * (同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。)
     */
    private long sourceId;

    public RtcpBye() {
    }

    public RtcpBye(long sourceId) {
        this.header = new RtcpHeader();
        this.header.version = 2;
        this.header.padding = false;
        this.header.receptionCount = 1;
        this.header.packageType = ERtcpPackageType.BYE;
        this.header.length = 1;
        this.sourceId = sourceId;
    }

    @Override
    public int byteArrayLength() {
        return 8;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(8)
                .putBytes(this.header.toByteArray())
                .putInteger(this.sourceId)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static RtcpBye fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static RtcpBye fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("RtcpBye, data length < 8");
        }
        RtcpBye res = new RtcpBye();
        res.header = RtcpHeader.fromBytes(data, offset);
        ByteReadBuff buff = new ByteReadBuff(data, offset + res.header.byteArrayLength());
        res.sourceId = buff.getUInt32();
        return res;
    }
}
