package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求写多个线圈
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbWriteMultipleCoilResponse extends MbPdu {

    /**
     * 输出地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 输出数量 0x0001 至 0x07B0 <br>
     * 字节大小：2个字节
     */
    private int quantity;

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 4;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putShort(this.address)
                .putShort(this.quantity)
                .getData();
    }

    public static MbWriteMultipleCoilResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbWriteMultipleCoilResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbWriteMultipleCoilResponse res = new MbWriteMultipleCoilResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.quantity = buff.getUInt16();
        return res;
    }
}
