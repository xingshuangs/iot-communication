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
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 响应头
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McHeader4EAck extends McHeader3EAck {

    /**
     * 序列号，2字节
     */
    protected int serialNumber = 0;

    /**
     * 固定值编号，2字节
     */
    protected int fixedNumber = 0;

    public McHeader4EAck() {
        this.frameType = EMcFrameType.FRAME_4E;
    }

    @Override
    public int byteArrayLength() {
        return 6 + this.accessRoute.byteArrayLength() + 2 + 2;
    }

    @Override
    public byte[] toByteArray() {
        int length = 6 + this.accessRoute.byteArrayLength() + 2 + 2;
        return ByteWriteBuff.newInstance(length, true)
                .putShort(this.subHeader)
                .putShort(this.serialNumber)
                .putShort(this.fixedNumber)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .putShort(this.endCode)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      字节数组数据
     * @return McHeaderAck
     */
    public static McHeader4EAck fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      字节数组数据
     * @param offset    偏移量
     * @return McHeaderAck
     */
    public static McHeader4EAck fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McHeader4EAck res = new McHeader4EAck();
        res.subHeader = buff.getUInt16();
        res.serialNumber = buff.getUInt16();
        res.fixedNumber = buff.getUInt16();
        res.accessRoute = McFrame4E3EAccessRoute.fromBytes(buff.getBytes(5));
        res.dataLength = buff.getUInt16();
        res.endCode = buff.getUInt16();
        return res;
    }
}
