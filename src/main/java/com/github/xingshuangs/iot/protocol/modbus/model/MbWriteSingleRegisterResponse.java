package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求写单个寄存器
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbWriteSingleRegisterResponse extends MbPdu {

    /**
     * 输出地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 输出值，0x0000为off，0xFF00为on
     * 字节大小：2个字节
     */
    private byte[] value;

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 4;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putShort(this.address)
                .putBytes(this.value)
                .getData();
    }

    public static MbWriteSingleRegisterResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbWriteSingleRegisterResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbWriteSingleRegisterResponse res = new MbWriteSingleRegisterResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.value = buff.getBytes(2);
        return res;
    }
}
