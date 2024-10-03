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


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设置通信参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetupComParameter extends Parameter implements IObjectByteArray {

    public static final int BYTE_LENGTH = 8;

    /**
     * 预留 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private byte reserved = (byte) 0x00;

    /**
     * Ack队列的大小（主叫）（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int maxAmqCaller = 0x0001;

    /**
     * Ack队列的大小（被叫）（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int maxAmqCallee = 0x0001;

    /**
     * PDU长度（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    private int pduLength = 0x0000;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.functionCode.getCode())
                .putByte(this.reserved)
                .putShort(this.maxAmqCaller)
                .putShort(this.maxAmqCallee)
                .putShort(this.pduLength)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return SetupComParameter
     */
    public static SetupComParameter fromBytes(final byte[] data) {
        ByteReadBuff buff = new ByteReadBuff(data);
        SetupComParameter setupComParameter = new SetupComParameter();
        setupComParameter.functionCode = EFunctionCode.from(buff.getByte());
        setupComParameter.reserved = buff.getByte();
        setupComParameter.maxAmqCaller = buff.getUInt16();
        setupComParameter.maxAmqCallee = buff.getUInt16();
        setupComParameter.pduLength = buff.getUInt16();
        return setupComParameter;
    }

    /**
     * 创建默认的设置通信参数，默认最大PDU长度240
     *
     * @param pduLength PDU长度
     * @return SetupComParameter
     */
    public static SetupComParameter createDefault(int pduLength) {
        SetupComParameter parameter = new SetupComParameter();
        parameter.functionCode = EFunctionCode.SETUP_COMMUNICATION;
        parameter.reserved = (byte) 0x00;
        parameter.maxAmqCaller = 1;
        parameter.maxAmqCallee = 1;
        // 默认最大PDU长度240
        parameter.pduLength = pduLength;
        return parameter;
    }
}
