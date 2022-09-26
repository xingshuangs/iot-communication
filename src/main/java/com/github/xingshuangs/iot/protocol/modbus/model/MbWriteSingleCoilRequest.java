package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求写单个线圈
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MbWriteSingleCoilRequest extends MbPdu {

    public static final byte[] ON = new byte[]{(byte) 0xFF, (byte) 0x00};

    public static final byte[] OFF = new byte[]{(byte) 0x00, (byte) 0x00};

    /**
     * 输出地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 输出值，0x0000为off，0xFF00为on
     * 字节大小：2个字节
     */
    private boolean value;

    public MbWriteSingleCoilRequest() {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_COIL;
    }

    public MbWriteSingleCoilRequest(int address, boolean value) {
        this.functionCode = EMbFunctionCode.WRITE_SINGLE_COIL;
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
                .putBytes(this.value ? ON : OFF)
                .getData();
    }

    public static MbWriteSingleCoilRequest fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbWriteSingleCoilRequest fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbWriteSingleCoilRequest res = new MbWriteSingleCoilRequest();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.address = buff.getUInt16();
        res.value = buff.getByte() == ON[0];
        return res;
    }
}
