package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 请求写单个寄存器
 *
 * @author xingshuang
 */
@Data
public final class MbWriteSingleRegisterRequest extends MbPdu {

    /**
     * 输出地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 寄存器值，0x0000 至 0xFFFF
     * 字节大小：2个字节
     */
    private int value;

    public MbWriteSingleRegisterRequest() {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_REGISTER;
    }

    public MbWriteSingleRegisterRequest(int address, int value) {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_REGISTER;
        this.address = address;
        this.value = value;
    }

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 4;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putShort(this.address)
                .putShort(this.value)
                .getData();
    }

    public static MbWriteSingleRegisterRequest fromBytes(final byte[] data) {
        return fromBytes(data,0);
    }

    public static MbWriteSingleRegisterRequest fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbWriteSingleRegisterRequest res = new MbWriteSingleRegisterRequest();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.value = buff.getUInt16();
        return res;
    }
}
