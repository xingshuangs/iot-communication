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

package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Nalu的FuB
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | FU indicator  |   FU header   |               DON             |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
 * |                                                               |
 * |                         FU payload                            |
 * |                                                               |
 * |                               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                               :...OPTIONAL RTP padding        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class H264NaluFuB extends H264NaluBase {

    /**
     * Fu的头
     */
    private H264NaluFuHeader fuHeader = new H264NaluFuHeader();

    /**
     * 解码顺序编号
     */
    private int decodingOrderNumber;

    /**
     * 负载
     */
    protected byte[] payload = new byte[0];

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + this.fuHeader.byteArrayLength() + 2 + this.payload.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putBytes(this.header.toByteArray())
                .putBytes(this.fuHeader.toByteArray())
                .putShort(this.decodingOrderNumber)
                .putBytes(this.payload)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluFuB fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluFuB fromBytes(final byte[] data, final int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("H264NaluSingle, data length < 1");
        }
        H264NaluFuB res = new H264NaluFuB();
        int index = offset;
        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        res.decodingOrderNumber = ShortUtil.toUInt16(data, index);
        index += 2;

        res.fuHeader = H264NaluFuHeader.fromBytes(data, index);
        index += res.fuHeader.byteArrayLength();

        ByteReadBuff buff = new ByteReadBuff(data, index);
        res.payload = buff.getBytes();
        return res;
    }
}
