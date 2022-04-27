package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
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
        return 3;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[3];
        res[0] = (byte) (this.getLength() & 0xFF);
        res[1] = this.getPduType().getCode();
        res[2] = (byte) (BooleanUtil.setBit((byte) 0x00, 7, this.lastDataUnit) | (this.tpduNumber & 0xFF));
        return res;
    }

    /**
     * 通过字节数组转换为COTPData对象
     *
     * @param data 字节数组
     * @return COTPData对象
     */
    public static COTPData fromBytes(byte[] data) {
        COTPData cotpData = new COTPData();
        cotpData.setLength(data[0] & 0xFF);
        cotpData.setPduType(EPduType.from(data[1]));
        cotpData.setTpduNumber(data[2] & 0x7F);
        cotpData.setLastDataUnit(BooleanUtil.getValue(data[2], 7));
        return cotpData;
    }
}
