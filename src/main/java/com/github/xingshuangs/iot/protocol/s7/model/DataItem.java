package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EDataVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回数据
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataItem extends ReturnItem implements IByteArray {

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
        return ByteWriteBuff.newInstance(4 + this.data.length)
                .putByte(this.returnCode.getCode())
                .putByte(this.variableType.getCode())
                // 如果数据类型是位，不需要 * 8，如果是其他类型，需要 * 8
                .putShort((this.count * (this.variableType == EDataVariableType.BIT ? 1 : 8)))
                .putBytes(this.data)
                .getData();
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
    public static DataItem byByte(byte data) {
        return byByte(new byte[]{data});
    }

    /**
     * 通过字节数组数据类型转换为DataItem数据
     *
     * @param data 字节数组
     * @return DataItem数据
     */
    public static DataItem byByte(byte[] data) {
        if (data == null) {
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
    public static DataItem byBoolean(boolean data) {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.RESERVED);
        dataItem.setVariableType(EDataVariableType.BIT);
        dataItem.setCount(1);
        dataItem.setData(new byte[]{BooleanUtil.toByte(data)});
        return dataItem;
    }
}
