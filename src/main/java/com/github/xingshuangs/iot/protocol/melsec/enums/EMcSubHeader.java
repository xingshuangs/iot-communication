package com.github.xingshuangs.iot.protocol.melsec.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 副帧头
 *
 * @author xingshuang
 */
public enum EMcSubHeader {
    /**
     * 4E帧请求
     */
    REQ_4E( 0x0054),

    /**
     * 4E帧响应
     */
    ACK_4E( 0x00D4),

    /**
     * 3E帧请求
     */
    REQ_3E( 0x0050),

    /**
     * 3E帧响应
     */
    ACK_3E( 0x00D0),
    ;

    private static Map<Integer, EMcSubHeader> map;

    public static EMcSubHeader from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcSubHeader item : EMcSubHeader.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    EMcSubHeader(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
