package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;

/**
 * @author xingshuang
 */
public class ParameterBuilder {
    public static Parameter fromBytes(final byte[] data) {
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
                return null;
            case PLC_STOP:
                return null;
            case SETUP_COMMUNICATION:
                return SetupComParameter.fromBytes(data);
            default:
                throw new S7CommException("Parameter的功能码不存在");
        }
    }
}
