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


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * Modbus PDU.
 * (modbus的协议数据单元)
 *
 * @author xingshuang
 */
@Data
public class MbPdu implements IObjectByteArray {

    /**
     * Function code.
     * (功能码)
     */
    protected EMbFunctionCode functionCode;

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(1)
                .putByte(this.functionCode.getCode())
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data byte array
     * @return MbPdu
     */
    public static MbPdu fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return mbpdu对象
     */
    public static MbPdu fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        byte aByte = buff.getByte();
        EMbFunctionCode functionCode = EMbFunctionCode.from(aByte);
        if (functionCode == null) {
            throw new ModbusCommException("Function code can not be recognized, " + aByte);
        }

        switch (functionCode) {
            case READ_COIL:
                return MbReadCoilResponse.fromBytes(data, offset);
            case READ_DISCRETE_INPUT:
                return MbReadDiscreteInputResponse.fromBytes(data, offset);
            case READ_HOLD_REGISTER:
                return MbReadHoldRegisterResponse.fromBytes(data, offset);
            case READ_INPUT_REGISTER:
                return MbReadInputRegisterResponse.fromBytes(data, offset);
            case WRITE_SINGLE_COIL:
                return MbWriteSingleCoilResponse.fromBytes(data, offset);
            case WRITE_SINGLE_REGISTER:
                return MbWriteSingleRegisterResponse.fromBytes(data, offset);
            case WRITE_MULTIPLE_COIL:
                return MbWriteMultipleCoilResponse.fromBytes(data, offset);
            case WRITE_MULTIPLE_REGISTER:
                return MbWriteMultipleRegisterResponse.fromBytes(data, offset);
            case ERROR_READ_COIL:
            case ERROR_READ_DISCRETE_INPUT:
            case ERROR_READ_HOLD_REGISTER:
            case ERROR_READ_INPUT_REGISTER:
            case ERROR_WRITE_SINGLE_COIL:
            case ERROR_WRITE_SINGLE_REGISTER:
            case ERROR_WRITE_MULTIPLE_COIL:
            case ERROR_WRITE_MULTIPLE_REGISTER:
                return MbErrorResponse.fromBytes(data, offset);
            default:
                throw new ModbusCommException("Function code can not be recognized：" + functionCode.getDescription());
        }
    }

    /**
     * Parses byte array and converts it to object, convert to request object.
     *
     * @param data byte array
     * @return MbPdu
     */
    public static MbPdu fromBytesToRequest(final byte[] data) {
        return fromBytesToRequest(data, 0);
    }

    /**
     * Parses byte array and converts it to object, convert to request object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return mbpdu对象
     */
    public static MbPdu fromBytesToRequest(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        byte aByte = buff.getByte();
        EMbFunctionCode functionCode = EMbFunctionCode.from(aByte);
        if (functionCode == null) {
            throw new ModbusCommException("Function code can not be recognized, " + aByte);
        }

        switch (functionCode) {
            case READ_COIL:
                return MbReadCoilRequest.fromBytes(data, offset);
            case READ_DISCRETE_INPUT:
                return MbReadDiscreteInputRequest.fromBytes(data, offset);
            case READ_HOLD_REGISTER:
                return MbReadHoldRegisterRequest.fromBytes(data, offset);
            case READ_INPUT_REGISTER:
                return MbReadInputRegisterRequest.fromBytes(data, offset);
            case WRITE_SINGLE_COIL:
                return MbWriteSingleCoilRequest.fromBytes(data, offset);
            case WRITE_SINGLE_REGISTER:
                return MbWriteSingleRegisterRequest.fromBytes(data, offset);
            case WRITE_MULTIPLE_COIL:
                return MbWriteMultipleCoilRequest.fromBytes(data, offset);
            case WRITE_MULTIPLE_REGISTER:
                return MbWriteMultipleRegisterRequest.fromBytes(data, offset);
            default:
                throw new ModbusCommException("Function code can not be recognized：" + functionCode.getDescription());
        }
    }
}
