package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 接收内容类型
 *
 * @author xingshuang
 */
public enum ERtspContentType {

    /**
     * sdp
     */
    SDP("application/sdp"),

    /**
     * parameter
     */
    PARAMETER("text/parameters"),

    ;

    private static Map<String, ERtspContentType> map;

    public static ERtspContentType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspContentType item : ERtspContentType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    ERtspContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
