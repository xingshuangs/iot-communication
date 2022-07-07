package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbExceptionCode;
import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 响应错误
 *
 * @author xingshuang
 */
@Data
public class MbErrorResponse extends MbPdu {

    private EMbExceptionCode errorCode;


    @Override
    public int byteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(2)
                .putByte(this.functionCode.getCode())
                .putByte(this.errorCode.getCode())
                .getData();
    }

    public static MbErrorResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbErrorResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbErrorResponse res = new MbErrorResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.errorCode = EMbExceptionCode.from(buff.getByte());
        return res;
    }
}
