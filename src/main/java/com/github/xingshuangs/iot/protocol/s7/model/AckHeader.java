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


import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 响应头
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AckHeader extends Header {

    public static final int BYTE_LENGTH = 12;

    /**
     * 错误类型 <br>
     * 字节大小：1 <br>
     * 字节序数：10
     */
    private EErrorClass errorClass = EErrorClass.NO_ERROR;

    /**
     * 错误码，本来是1个字节的，但本质上errorCode（真正） = errorClass + errorCode（原） <br>
     * 字节大小：2 <br>
     * 字节序数：10-11
     */
    private int errorCode = 0x0000;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putBytes(super.toByteArray())
                .putByte(errorClass.getCode())
                .putByte(this.errorCode)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data byte array
     * @return AckHeader
     */
    public static AckHeader fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new IndexOutOfBoundsException("header, data length < 12");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        AckHeader header = new AckHeader();
        header.protocolId = buff.getByte();
        header.messageType = EMessageType.from(buff.getByte());
        header.reserved = buff.getUInt16();
        header.pduReference = buff.getUInt16();
        header.parameterLength = buff.getUInt16();
        header.dataLength = buff.getUInt16();
        header.errorClass = EErrorClass.from(buff.getByte());
        header.errorCode = buff.getUInt16(10);
        return header;
    }

    /**
     * 创建默认的头header
     *
     * @param request    请求头
     * @param errorClass 错误类
     * @param errorCode  错误码
     * @return Header对象
     */
    public static AckHeader createDefault(Header request, EErrorClass errorClass, int errorCode) {
        AckHeader header = new AckHeader();
        header.protocolId = request.protocolId;
        header.messageType = EMessageType.ACK_DATA;
        header.reserved = request.reserved;
        header.pduReference = request.pduReference;
        header.parameterLength = request.parameterLength;
        header.dataLength = request.dataLength;
        header.errorClass = errorClass;
        header.errorCode = errorCode;
        return header;
    }
}
