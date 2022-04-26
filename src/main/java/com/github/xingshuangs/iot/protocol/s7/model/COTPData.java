package com.github.xingshuangs.iot.protocol.s7.model;


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
    private byte tpduNumber = 0x00;

    /**
     * 是否最后一个数据单元 <br>
     * 字节大小：1，最高位 <br>
     * 字节序数：2
     */
    private boolean lastDataUnit = true;

    @Override
    public int getByteArrayLength() {
        return 3;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[3];
        res[0] = this.getLength();
        res[1] = this.getPduType().getCode();
        res[2] = (byte) (BooleanUtil.setBit((byte) 0x00, 7, this.lastDataUnit) | this.tpduNumber);
        return res;
    }
}
