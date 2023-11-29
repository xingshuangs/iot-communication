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


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求写单个线圈
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbWriteSingleCoilRequest extends MbPdu {

    public static final byte[] ON = new byte[]{(byte) 0xFF, (byte) 0x00};

    public static final byte[] OFF = new byte[]{(byte) 0x00, (byte) 0x00};

    /**
     * 输出地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 输出值，0x0000为off，0xFF00为on
     * 字节大小：2个字节
     */
    private boolean value;

    public MbWriteSingleCoilRequest() {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_COIL;
    }

    public MbWriteSingleCoilRequest(int address, boolean value) {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_COIL;
        this.address = address;
        this.value = value;
    }

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 4;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putShort(this.address)
                .putBytes(this.value ? ON : OFF)
                .getData();
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return MbWriteSingleCoilRequest
     */
    public static MbWriteSingleCoilRequest fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return MbWriteSingleCoilRequest
     */
    public static MbWriteSingleCoilRequest fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbWriteSingleCoilRequest res = new MbWriteSingleCoilRequest();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.value = buff.getByte() == ON[0];
        return res;
    }
}
