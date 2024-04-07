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
import com.github.xingshuangs.iot.protocol.s7.enums.EDataVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回数据
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataItem extends ReturnItem implements IObjectByteArray {

    /**
     * 变量类型 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private EDataVariableType variableType = EDataVariableType.BYTE_WORD_DWORD;

    /**
     * 数据长度，按位进行计算的，如果是字节数据读取需要进行 /8 或 *8操作，如果是位数据，不需要任何额外操作 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int count = 0x0000;

    /**
     * 数据内容
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return 4 + this.data.length;
    }

    @Override
    public byte[] toByteArray() {
        int length = 4 + this.data.length;
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length)
                .putByte(this.returnCode.getCode())
                .putByte(this.variableType.getCode());
        // 如果数据类型是位，不需要 * 8，如果是其他类型，需要 * 8
        switch (this.variableType) {
            case NULL:
            case BYTE_WORD_DWORD:
            case INTEGER:
                buff.putShort(this.count * 8);
                break;
            case BIT:
            case DINTEGER:
            case REAL:
            case OCTET_STRING:
                buff.putShort(this.count);
                break;
            default:
                throw new S7CommException("Data type not recognized");
        }
        buff.putBytes(this.data);
        return buff.getData();
    }

    /**
     * 复制一个新对象
     *
     * @return DataItem
     */
    public DataItem copy() {
        DataItem dataItem = new DataItem();
        dataItem.returnCode = this.returnCode;
        dataItem.variableType = this.variableType;
        dataItem.count = this.count;
        dataItem.data = this.data;
        return dataItem;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return DataItem
     */
    public static DataItem fromBytes(final byte[] data) {
        ByteReadBuff buff = new ByteReadBuff(data);
        DataItem dataItem = new DataItem();
        dataItem.returnCode = EReturnCode.from(buff.getByte());
        dataItem.variableType = EDataVariableType.from(buff.getByte());
        // 如果是bit，正常解析，如果是字节，则需要除8操作
        switch (dataItem.variableType) {
            case NULL:
            case BYTE_WORD_DWORD:
            case INTEGER:
                dataItem.count = buff.getUInt16() / 8;
                break;
            case BIT:
            case DINTEGER:
            case REAL:
            case OCTET_STRING:
                dataItem.count = buff.getUInt16();
                break;
            default:
                throw new S7CommException("Data type not recognized");
        }
        // 返回数据类型为null，那就是没有数据
        if (dataItem.variableType != EDataVariableType.NULL) {
            dataItem.data = buff.getBytes(dataItem.count);
        }
        return dataItem;
    }

    /**
     * 通过字节数据类型转换为DataItem数据
     *
     * @param data 字节数据
     * @return DataItem数据
     */
    public static DataItem createReqByByte(byte data) {
        return createReqByByte(new byte[]{data});
    }

    /**
     * 通过字节数组数据类型转换为DataItem数据
     *
     * @param data 字节数组
     * @return DataItem数据
     */
    public static DataItem createReqByByte(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data length is null or empty");
        }
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.RESERVED);
        dataItem.setVariableType(EDataVariableType.BYTE_WORD_DWORD);
        dataItem.setCount(data.length);
        dataItem.setData(data);
        return dataItem;
    }

    /**
     * 通过boolean数据类型转换为DataItem数据
     *
     * @param data boolean数据
     * @return DataItem数据
     */
    public static DataItem createReqByBoolean(boolean data) {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.RESERVED);
        dataItem.setVariableType(EDataVariableType.BIT);
        dataItem.setCount(1);
        dataItem.setData(new byte[]{BooleanUtil.toByte(data)});
        return dataItem;
    }

    /**
     * 通过字节数组+数据类型转换为DataItem数据
     *
     * @param data             字节数组
     * @param dataVariableType 数据类型
     * @return 数据项目
     */
    public static DataItem createReq(byte[] data, EDataVariableType dataVariableType) {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.RESERVED);
        dataItem.setVariableType(dataVariableType);
        dataItem.setCount(data.length);
        dataItem.setData(data);
        return dataItem;
    }

    /**
     * 通过字节数组+数据类型转换为DataItem数据
     *
     * @param data             字节数组
     * @param dataVariableType 数据类型
     * @return 数据项目
     */
    public static DataItem createAckBy(byte[] data, EDataVariableType dataVariableType) {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.SUCCESS);
        dataItem.setVariableType(dataVariableType);
        dataItem.setCount(data.length);
        dataItem.setData(data);
        return dataItem;
    }
}
