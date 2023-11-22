package com.github.xingshuangs.iot.protocol.melsec.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 副帧头
 *
 * @author xingshuang
 */
public enum EMcFrameType {

    /**
     * 4E
     */
    FRAME_4E("4E", 0x0054, 0x00D4),

    /**
     * 3E
     */
    FRAME_3E("3E", 0x0050, 0x00D0),
    ;

    private static Map<String, EMcFrameType> map;

    public static EMcFrameType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcFrameType item : EMcFrameType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    private final int reqSubHeader;

    private final int ackSubHeader;

    EMcFrameType(String code, int reqSubHeader, int ackSubHeader) {
        this.code = code;
        this.reqSubHeader = reqSubHeader;
        this.ackSubHeader = ackSubHeader;
    }

    public String getCode() {
        return code;
    }

    public int getReqSubHeader() {
        return reqSubHeader;
    }

    public int getAckSubHeader() {
        return ackSubHeader;
    }
}
