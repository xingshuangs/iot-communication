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

    public static MbPdu fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return mbpdu对象
     */
    public static MbPdu fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        EMbFunctionCode functionCode = EMbFunctionCode.from(buff.getByte());

        switch (functionCode) {
            case READ_COIL:
                return MbReadCoilResponse.fromBytes(data, offset);
            case READ_DISCRETE_INPUT:
                return MbReadDiscreteInputResponse.fromBytes(data, offset);
            case READ_HOLD_REGISTER:
                return MbReadHoldRegisterResponse.fromBytes(data, offset);
            case READ_INPUT_REGISTER:
                return MbReadInputRegisterResponse.fromBytes(data, offset);
            case WRITE_SINGLE_COIL:
                return MbWriteSingleCoilResponse.fromBytes(data, offset);
            case WRITE_SINGLE_REGISTER:
                return MbWriteSingleRegisterResponse.fromBytes(data, offset);
            case WRITE_MULTIPLE_COIL:
                return MbWriteMultipleCoilResponse.fromBytes(data, offset);
            case WRITE_MULTIPLE_REGISTER:
                return MbWriteMultipleRegisterResponse.fromBytes(data, offset);
            case ERROR_READ_COIL:
            case ERROR_READ_DISCRETE_INPUT:
            case ERROR_READ_HOLD_REGISTER:
            case ERROR_READ_INPUT_REGISTER:
            case ERROR_WRITE_SINGLE_COIL:
            case ERROR_WRITE_SINGLE_REGISTER:
            case ERROR_WRITE_MULTIPLE_COIL:
            case ERROR_WRITE_MULTIPLE_REGISTER:
                return MbErrorResponse.fromBytes(data, offset);
            default:
                throw new ModbusCommException("无法识别功能码：" + functionCode.getDescription());
        }
    }
}
