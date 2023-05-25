package com.github.xingshuangs.iot.protocol.rtp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 帧的类别
 *
 * @author xingshuang
 */
public enum EFrameType {
    /**
     * 问题
     */
    PROBLEM((byte) 0x00),

    /**
     * 视频
     */
    VIDEO((byte) 0x01),

    /**
     * 音频
     */
    AUDIO((byte) 0x02),

    ;

    private static Map<Byte, EFrameType> map;

    public static EFrameType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EFrameType item : EFrameType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EFrameType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
