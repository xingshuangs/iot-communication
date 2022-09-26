package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求读输入寄存器
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbReadInputRegisterRequest extends MbPdu {

    /**
     * 起始地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 输入寄存器数量，该功能码读取线圈的 1 至 2000 连续状态，1 至 2000（0x7D0）
     * 字节大小：2个字节
     */
    private int quantity;

    public MbReadInputRegisterRequest() {
        this.functionCode = EMbFunctionCode.READ_INPUT_REGISTER;
    }

    public MbReadInputRegisterRequest(int address, int quantity) {
        this.functionCode = EMbFunctionCode.READ_INPUT_REGISTER;
        this.address = address;
        this.quantity = quantity;
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
                .putShort(this.quantity)
                .getData();
    }

    public static MbReadInputRegisterRequest fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbReadInputRegisterRequest fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbReadInputRegisterRequest res = new MbReadInputRegisterRequest();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.quantity = buff.getUInt16();
        return res;
    }
}
