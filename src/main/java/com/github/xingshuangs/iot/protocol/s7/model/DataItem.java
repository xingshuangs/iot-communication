package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EDataVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
        // 如果数据长度为奇数，S7协议会多填充一个字节，使其保持为偶数（最后一个奇数长度数据不需要填充）
        return 4 + this.data.length + (this.data.length % 2 == 0 ? 0 : 1);
    }

    @Override
    public byte[] toByteArray() {
        // 如果数据长度为奇数，S7协议会多填充一个字节，使其保持为偶数（最后一个奇数长度数据不需要填充）
        int length = 4 + this.data.length + (this.data.length % 2 == 0 ? 0 : 1);
        return ByteWriteBuff.newInstance(length)
                .putByte(this.returnCode.getCode())
                .putByte(this.variableType.getCode())
                // 如果数据类型是位，不需要 * 8，如果是其他类型，需要 * 8
                .putShort((this.count * (this.variableType == EDataVariableType.BIT ? 1 : 8)))
                .putBytes(this.data)
                .getData();
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
        dataItem.count = buff.getUInt16() / (dataItem.variableType == EDataVariableType.BIT ? 1 : 8);
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
    public static DataItem createByByte(byte data) {
        return createByByte(new byte[]{data});
    }

    /**
     * 通过字节数组数据类型转换为DataItem数据
     *
     * @param data 字节数组
     * @return DataItem数据
     */
    public static DataItem createByByte(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data数据不能为null");
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
    public static DataItem createByBoolean(boolean data) {
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
    public static DataItem createByByte(byte[] data, EDataVariableType dataVariableType) {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.RESERVED);
        dataItem.setVariableType(dataVariableType);
        dataItem.setCount(data.length);
        dataItem.setData(data);
        return dataItem;
    }
}
