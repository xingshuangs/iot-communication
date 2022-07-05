package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 响应读取线圈
 *
 * @author xingshuang
 */
@Data
public final class MbReadHoldRegisterResponse extends MbPdu {

    /**
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * 寄存器值，N＝寄存器的数量，N*×2 个字节
     * 字节大小：N*×2 个字节
     */
    private byte[] register;

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 1 + this.register.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putByte(this.count)
                .putBytes(this.register)
                .getData();
    }

    public static MbReadHoldRegisterResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbReadHoldRegisterResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbReadHoldRegisterResponse res = new MbReadHoldRegisterResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.count = buff.getByteToInt();
        res.register = buff.getBytes(res.count);
        return res;
    }
}
