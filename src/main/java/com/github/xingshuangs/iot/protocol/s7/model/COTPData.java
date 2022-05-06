package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ByteUtil;
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
        byte[] res = new byte[BYTE_LENGTH];
        res[0] = ByteUtil.toByte(this.length);
        res[1] = this.pduType.getCode();
        res[2] = (byte) (BooleanUtil.setBit((byte) 0x00, 7, this.lastDataUnit) | (this.tpduNumber & 0xFF));
        return res;
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
        COTPData cotpData = new COTPData();
        cotpData.length = ByteUtil.toUInt8(data[0]);
        cotpData.pduType = EPduType.from(data[1]);
        cotpData.tpduNumber = data[2] & 0x7F;
        cotpData.lastDataUnit = BooleanUtil.getValue(data[2], 7);
        return cotpData;
    }

    /**
     * Connect DtData 连接请求
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
