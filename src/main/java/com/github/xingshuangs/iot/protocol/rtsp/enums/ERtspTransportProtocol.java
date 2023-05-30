package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 传输协议，TCP/UDP
 *
 * @author xingshuang
 */
public enum ERtspTransportProtocol {

    /**
     * TCP
     */
    TCP(0),

    /**
     * UDP
     */
    UDP(1),

    ;

    private static Map<Integer, ERtspTransportProtocol> map;

    public static ERtspTransportProtocol from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspTransportProtocol item : ERtspTransportProtocol.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    ERtspTransportProtocol(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
