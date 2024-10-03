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

/**
 * @author xingshuang
 */
public class H264NaluStapSingle extends H264NaluSingle {

    private int size;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + 2 + this.payload.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putShort(this.size)
                .putBytes(this.header.toByteArray())
                .putBytes(this.payload)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static H264NaluStapSingle fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static H264NaluStapSingle fromBytes(final byte[] data, final int offset) {
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("H264NaluStapSingle, data length < 1");
        }
        int index = offset;
        H264NaluStapSingle res = new H264NaluStapSingle();
        res.size = ByteReadBuff.newInstance(data, index).getUInt16();
        index += 2;

        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        res.payload = ByteReadBuff.newInstance(data, index).getBytes(res.size - 1);
        return res;
    }
}
