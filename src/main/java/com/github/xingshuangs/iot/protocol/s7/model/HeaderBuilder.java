package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;

/**
 * Header构建器
 *
 * @author xingshuang
 */
public class HeaderBuilder {

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return Header
     */
    public static Header fromBytes(final byte[] data) {
        EMessageType messageType = EMessageType.from(data[1]);

        switch (messageType) {
            case JOB:
                return Header.fromBytes(data);
            case ACK:
                return null;
            case ACK_DATA:
                return AckHeader.fromBytes(data);
            case USER_DATA:
                return null;
            default:
                throw new S7CommException("COTP的pduType数据类型无法解析");
        }
    }
}
