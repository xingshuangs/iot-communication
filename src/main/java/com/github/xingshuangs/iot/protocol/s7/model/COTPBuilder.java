package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.ByteUtil;

import java.util.Arrays;

/**
 * @author xingshuang
 */
public class COTPBuilder {

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return COTP
     */
    public static COTP fromBytes(final byte[] data) {
        int length = ByteUtil.toUInt8(data[0]);
        byte[] cotpBytes = Arrays.copyOfRange(data, 0, length + 1);

        EPduType pduType = EPduType.from(cotpBytes[1]);

        switch (pduType) {
            case CONNECT_REQUEST:
            case CONNECT_CONFIRM:
            case DISCONNECT_REQUEST:
            case DISCONNECT_CONFIRM:
                return COTPConnection.fromBytes(cotpBytes);
            case REJECT:
                return null;
            case DT_DATA:
                return COTPData.fromBytes(cotpBytes);
            default:
                throw new S7CommException("COTP的pduType数据类型无法解析");
        }
    }
}
