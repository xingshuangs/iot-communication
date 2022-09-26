package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * COTP数据部分Describes a COTP TPDU (Transport protocol data unit)
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class COTPData extends COTP implements IByteArray {

    public static final int BYTE_LENGTH = 3;
    /**
     * TPDU编号 <br>
     * 字节大小：1，后面7位 <br>
     * 字节序数：2
     */
    private int tpduNumber = 0x00;

    /**
     * 是否最后一个数据单元 <br>
     * 字节大小：1，最高位，7位 <br>
     * 字节序数：2
     */
    private boolean lastDataUnit = true;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.length)
                .putByte(this.pduType.getCode())
                // TPDU编号和是否最后一个数据单元组合成一个字节，最高位表示是否最后一个
                .putByte((byte) (BooleanUtil.setBit((byte) 0x00, 7, this.lastDataUnit) | (this.tpduNumber & 0xFF)))
                .getData();
    }

    /**
     * 通过字节数组转换为COTPData对象
     *
     * @param data 字节数组
     * @return COTPData对象
     */
    public static COTPData fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new S7CommException("COTPData数据字节长度不够，无法解析");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        COTPData cotpData = new COTPData();
        cotpData.length = buff.getByteToInt();
        cotpData.pduType = EPduType.from(buff.getByte());
        cotpData.tpduNumber = buff.getByte() & 0x7F;
        cotpData.lastDataUnit = buff.getBoolean(2,7);
        return cotpData;
    }

    /**
     * DtData COTP 数据部分
     *
     * @return COTPData对象
     */
    public static COTPData createDefault() {
        COTPData connection = new COTPData();
        connection.length = 2;
        connection.pduType = EPduType.DT_DATA;
        connection.tpduNumber = 0;
        connection.lastDataUnit = true;
        return connection;
    }
}
