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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PLC control, ack parameter.
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlcControlAckParameter extends Parameter implements IObjectByteArray {

    /**
     * unknown byte.
     */
    private byte unknownByte;

    public PlcControlAckParameter() {
        this.functionCode = EFunctionCode.PLC_CONTROL;
    }

    @Override
    public int byteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(2)
                .putByte(this.functionCode.getCode())
                .putByte(this.unknownByte)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return PlcControlAckParameter
     */
    public static PlcControlAckParameter fromBytes(final byte[] data) {
        if (data.length < 1) {
            // PlcControlAckParameter解析有误，PlcControlAckParameter字节数组长度 < 1
            throw new S7CommException("PlcControlAckParameter parsing error, PlcControlAckParameter byte array length < 1");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        PlcControlAckParameter parameter = new PlcControlAckParameter();
        parameter.functionCode = EFunctionCode.from(buff.getByte());
        parameter.unknownByte = buff.getByte();
        return parameter;
    }
}
