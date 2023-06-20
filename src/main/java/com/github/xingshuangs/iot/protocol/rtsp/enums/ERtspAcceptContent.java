package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 接收内容类型
 *
 * @author xingshuang
 */
public enum ERtspAcceptContent {

    /**
     * sdp
     */
    SDP("application/sdp"),

    /**
     * rtsl
     */
    RTSL("application/rtsl"),


    /**
     * mheg
     */
    MHEG("application/mheg"),

    ;

    private static Map<String, ERtspAcceptContent> map;

    public static ERtspAcceptContent from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspAcceptContent item : ERtspAcceptContent.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    ERtspAcceptContent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
