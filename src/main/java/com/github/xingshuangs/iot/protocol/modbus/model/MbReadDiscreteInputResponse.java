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
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Read discrete input response.
 * (响应读离散量输入)
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbReadDiscreteInputResponse extends MbPdu {

    /**
     * Byte count.
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * Input status.
     * 线圈状态N 或 N+1，N＝输出数量/8，如果余数不等于 0，那么N = N+1
     * 字节大小：N个字节
     */
    private byte[] inputStatus;

    public MbReadDiscreteInputResponse() {
        this.functionCode = EMbFunctionCode.READ_DISCRETE_INPUT;
    }

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 1 + this.inputStatus.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putByte(this.count)
                .putBytes(this.inputStatus)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data byte array
     * @return MbReadDiscreteInputResponse
     */
    public static MbReadDiscreteInputResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data   byte array
     * @param offset index offset
     * @return MbReadDiscreteInputResponse
     */
    public static MbReadDiscreteInputResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbReadDiscreteInputResponse res = new MbReadDiscreteInputResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.count = buff.getByteToInt();
        res.inputStatus = buff.getBytes(res.count);
        return res;
    }
}
