package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * PDU类型（CRConnect Request 连接请求）
 *
 * @author xingshuang
 */
public enum EPduType {

    /**
     * 连接请求
     */
    CONNECT_REQUEST((byte) 0xE0),

    /**
     * 连接确认
     */
    CONNECT_CONFIRM((byte) 0xD0),

    /**
     * 断开请求
     */
    DISCONNECT_REQUEST((byte) 0x80),

    /**
     * 断开确认
     */
    DISCONNECT_CONFIRM((byte) 0xC0),

    /**
     * 拒绝
     */
    REJECT((byte) 0x50),

    /**
     * 数据
     */
    DT_DATA((byte) 0xF0),
    ;

    private static Map<Byte, EPduType> map;

    public static EPduType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EPduType item : EPduType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EPduType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
