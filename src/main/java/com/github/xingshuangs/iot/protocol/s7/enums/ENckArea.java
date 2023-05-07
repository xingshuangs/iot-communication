package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * NCK的Area
 *
 * @author xingshuang
 */
public enum ENckArea {

    /**
     * NCK
     */
    N_NCK((byte) 0x00),

    /**
     * mode group
     */
    B_MODE_GROUP((byte) 0x01),

    /**
     * channel
     */
    C_CHANNEL((byte) 0x02),

    /**
     * axis
     */
    A_AXIS((byte) 0x03),

    /**
     * tool
     */
    T_TOOL((byte) 0x04),

    /**
     * feed drive
     */
    V_FEED_DRIVE((byte) 0x05),

    /**
     * main drive
     */
    M_MAIN_DRIVE((byte) 0x06),

    /**
     * mmc
     */
    M_MMC((byte) 0x07),

    ;

    private static Map<Byte, ENckArea> map;

    public static ENckArea from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ENckArea item : ENckArea.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    ENckArea(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
