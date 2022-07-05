package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * modbus的协议数据单元
 *
 * @author xingshuang
 */
@Data
public class MbPdu implements IByteArray {

    /**
     * 功能码
     */
    protected EMbFunctionCode functionCode;

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(1)
                .putByte(this.functionCode.getCode())
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return COTP
     */
    public static MbPdu fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);

        EMbFunctionCode functionCode = EMbFunctionCode.from(buff.getByte());

        switch (functionCode) {
            case READ_COIL:
                break;
            case READ_DISCRETE_INPUT:
            case READ_HOLD_REGISTER:
            case READ_INPUT_REGISTER:
            case WRITE_SINGLE_COIL:
            case WRITE_SINGLE_REGISTER:
            case WRITE_MULTIPLE_COIL:
            case WRITE_MULTIPLE_REGISTER:
            default:
                throw new ModbusCommException("无法识别功能码：" + functionCode.getDescription());
        }
        return null;
    }
}
