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

package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 报文头
 *
 * @author xingshuang
 */
@Data
public class MbapHeader implements IObjectByteArray {

    private static final AtomicInteger index = new AtomicInteger();

    public static final int BYTE_LENGTH = 7;

    /**
     * 事务元标识符<br>
     * 字节大小：2个字节
     * 字节序数：0-1
     */
    private int transactionId;

    /**
     * 协议标识符<br>
     * 字节大小：2个字节
     * 字节序数：2-3
     */
    private int protocolId = 0;

    /**
     * 长度，长度域是下一个域的字节数，包括单元标识符和数据域。length后面的所有字节长度，不包含前面和length<br>
     * 字节大小：2个字节
     * 字节序数：4-5
     */
    private int length = 0;

    /**
     * 单元标识符<br>
     * 字节大小：1个字节
     * 字节序数：6
     */
    private int unitId = 0;

    public MbapHeader() {
    }

    public MbapHeader(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putShort(this.transactionId)
                .putShort(this.protocolId)
                .putShort(this.length)
                .putByte(this.unitId)
                .getData();
    }

    /**
     * 获取新的pduNumber
     *
     * @return 编号
     */
    public static int getNewNumber() {
        int res = index.getAndIncrement();
        if (res >= 65536) {
            index.set(0);
            res = 0;
        }
        return res;
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return MbapHeader
     */
    public static MbapHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return MbapHeader
     */
    public static MbapHeader fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbapHeader res = new MbapHeader();
        res.transactionId = buff.getUInt16();
        res.protocolId = buff.getUInt16();
        res.length = buff.getUInt16();
        res.unitId = buff.getByteToInt();
        return res;
    }
}
