package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbExceptionCode;
import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 响应错误
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MbErrorResponse extends MbPdu {

    /**
     * 错误码
     */
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

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return MbErrorResponse
     */
    public static MbErrorResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return MbErrorResponse
     */
    public static MbErrorResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbErrorResponse res = new MbErrorResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.errorCode = EMbExceptionCode.from(buff.getByte());
        return res;
    }
}
