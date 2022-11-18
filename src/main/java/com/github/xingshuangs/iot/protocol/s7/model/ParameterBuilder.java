package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;

/**
 * @author xingshuang
 */
public class ParameterBuilder {

    private ParameterBuilder() {
        // NOOP
    }

    /**
     * 字节数组数据解析
     *
     * @param data        字节数组数据
     * @param messageType 消息类型
     * @return Parameter
     */
    public static Parameter fromBytes(final byte[] data, EMessageType messageType) {
        EFunctionCode functionCode = EFunctionCode.from(data[0]);

        switch (functionCode) {
            case CPU_SERVICES:
                return null;
            case READ_VARIABLE:
            case WRITE_VARIABLE:
                return ReadWriteParameter.fromBytes(data);
            case REQUEST_DOWNLOAD:
                return null;
            case DOWNLOAD_BLOCK:
                return null;
            case DOWNLOAD_ENDED:
                return null;
            case START_UPLOAD:
                return null;
            case UPLOAD:
                return null;
            case END_UPLOAD:
                return null;
            case PLC_CONTROL:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.PLC_CONTROL) : PlcControlParameter.fromBytes(data);
            case PLC_STOP:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.PLC_STOP) : PlcStopParameter.fromBytes(data);
            case SETUP_COMMUNICATION:
                return SetupComParameter.fromBytes(data);
            default:
                throw new S7CommException("Parameter的功能码不存在");
        }
    }
}
