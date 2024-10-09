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

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ack data.
 * 协议体数据：响应数据
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McAckData extends McData {

    /**
     * Data content.
     * (数据内容)
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return this.data.length;
    }

    @Override
    public byte[] toByteArray() {
        return this.data;
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data byte array
     * @return McAckData
     */
    public static McAckData fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data   byte array
     * @param offset index offset
     * @return McAckData
     */
    public static McAckData fromBytes(final byte[] data, final int offset) {
        if (data.length == 0) {
            return new McAckData();
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McAckData res = new McAckData();
        res.data = buff.getBytes();
        return res;
    }
}
