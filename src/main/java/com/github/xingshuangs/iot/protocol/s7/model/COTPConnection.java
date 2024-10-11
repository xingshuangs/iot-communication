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
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * COTP connection class.
 * COTP连接部分
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class COTPConnection extends COTP implements IObjectByteArray {

    public static final int BYTE_LENGTH = 18;

    /**
     * Destination reference, used to uniquely identify the target.
     * 目标引用，用来唯一标识目标 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int destinationReference = 0x0000;

    /**
     * Source reference.
     * 源引用 <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int sourceReference = 0x0001;

    /**
     * Extended format/flow control.
     * 扩展格式/流控制  前四位标识Class，  倒数第二位Extended formats，  倒数第一位No explicit flow control <br>
     * 字节大小：1 <br>
     * 字节序数：6
     */
    private byte flags = (byte) 0x00;

    /**
     * Parameter code tpdu size.
     * 参数代码TPDU-Size <br>
     * 字节大小：1 <br>
     * 字节序数：7
     */
    private byte parameterCodeTpduSize = (byte) 0xC0;

    /**
     * Tpdu size byte length.
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private int parameterLength1 = (byte) 0x01;

    /**
     * TPDU大小 TPDU Size (2^10 = 1024) <br>
     * 字节大小：1 <br>
     * 字节序数：9
     */
    private int tpduSize = (byte) 0x0A;

    /**
     * 参数代码SRC-TASP <br>
     * 字节大小：1 <br>
     * 字节序数：10
     */
    private byte parameterCodeSrcTsap = (byte) 0xC1;

    /**
     * Source tsap byte length.
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：11
     */
    private int parameterLength2 = (byte) 0x02;

    /**
     * SourceTSAP/Rack <br>
     * 字节大小：2 <br>
     * 字节序数：12-13
     */
    private int sourceTsap = 0x0100;

    /**
     * 参数代码DST-TASP <br>
     * 字节大小：1 <br>
     * 字节序数：14
     */
    private byte parameterCodeDstTsap = (byte) 0xC2;

    /**
     * Destination tsap byte length.
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：15
     */
    private int parameterLength3 = (byte) 0x02;

    /**
     * DestinationTSAP/Slot <br>
     * 字节大小：2 <br>
     * 字节序数：16-17
     */
    private int destinationTsap = 0x0100;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.length)
                .putByte(this.pduType.getCode())
                .putShort(this.destinationReference)
                .putShort(this.sourceReference)
                .putByte(this.flags)
                .putByte(this.parameterCodeTpduSize)
                .putByte(this.parameterLength1)
                .putByte(this.tpduSize)
                .putByte(this.parameterCodeSrcTsap)
                .putByte(this.parameterLength2)
                .putShort(this.sourceTsap)
                .putByte(this.parameterCodeDstTsap)
                .putByte(this.parameterLength3)
                .putShort(this.destinationTsap)
                .getData();
    }

    /**
     * CRConnect Request object.
     *
     * @param local  source tsap
     * @param remote destination tsap
     * @return COTPConnection
     */
    public static COTPConnection crConnectRequest(int local, int remote) {
        COTPConnection connection = new COTPConnection();
        connection.length = 0x11;
        connection.pduType = EPduType.CONNECT_REQUEST;
        connection.destinationReference = 0x0000;
        connection.sourceReference = 0x0001;
        connection.flags = (byte) 0x00;
        connection.parameterCodeTpduSize = (byte) 0xC0;
        connection.parameterLength1 = (byte) 0x01;
        connection.tpduSize = (byte) 0x0A;
        connection.parameterCodeSrcTsap = (byte) 0xC1;
        connection.parameterLength2 = (byte) 0x02;
        connection.sourceTsap = local;
        connection.parameterCodeDstTsap = (byte) 0xC2;
        connection.parameterLength3 = (byte) 0x02;
        connection.destinationTsap = remote;
        return connection;
    }

    /**
     * CRConnect Request object.
     *
     * @param request request data
     * @return COTPConnection
     */
    public static COTPConnection crConnectConfirm(COTPConnection request) {
        COTPConnection connection = new COTPConnection();
        connection.length = 0x11;
        connection.pduType = EPduType.CONNECT_CONFIRM;
        connection.destinationReference = 0x0001;
        connection.sourceReference = request.sourceReference;
        connection.flags = request.flags;
        connection.parameterCodeTpduSize = request.parameterCodeTpduSize;
        connection.parameterLength1 = request.parameterLength1;
        connection.tpduSize = request.tpduSize;
        connection.parameterCodeSrcTsap = request.parameterCodeSrcTsap;
        connection.parameterLength2 = request.parameterLength2;
        connection.sourceTsap = request.sourceTsap;
        connection.parameterCodeDstTsap = request.parameterCodeDstTsap;
        connection.parameterLength3 = request.parameterLength3;
        connection.destinationTsap = request.destinationTsap;
        return connection;
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return COTPConnection
     */
    public static COTPConnection fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            // COTPConnection数据字节长度不够，无法解析
            throw new S7CommException("The COTPConnection data bytes are not long enough to parse");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        COTPConnection connection = new COTPConnection();
        connection.length = buff.getByteToInt();
        connection.pduType = EPduType.from(buff.getByte());
        connection.destinationReference = buff.getUInt16();
        connection.sourceReference = buff.getUInt16();
        connection.flags = buff.getByte();
        connection.parameterCodeTpduSize = buff.getByte();
        connection.parameterLength1 = buff.getByteToInt();
        connection.tpduSize = buff.getByteToInt();
        connection.parameterCodeSrcTsap = buff.getByte();
        connection.parameterLength2 = buff.getByteToInt();
        connection.sourceTsap = buff.getUInt16();
        connection.parameterCodeDstTsap = buff.getByte();
        connection.parameterLength3 = buff.getByteToInt();
        connection.destinationTsap = buff.getUInt16();
        return connection;
    }
}
