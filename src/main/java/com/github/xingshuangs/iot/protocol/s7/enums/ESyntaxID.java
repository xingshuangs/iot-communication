package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 寻址模式和项结构其余部分的格式，它具有任意类型寻址的常量值0x10
 *
 * @author xingshuang
 */
public enum ESyntaxID {
    /**
     * Address data S7-Any pointer-like   DB1.DBX10.2
     */
    S7ANY((byte) 0x10),

    /**
     * R_ID for PBC
     */
    PBC_R_ID((byte) 0x13),

    /**
     * Alarm lock/free dataset
     */
    ALARM_LOCKFREE((byte) 0x15),

    /**
     * Alarm indication dataset
     */
    ALARM_IND((byte) 0x16),

    /**
     * Alarm acknowledge message dataset
     */
    ALARM_ACK((byte) 0x19),

    /**
     * Alarm query request dataset
     */
    ALARM_QUERYREQ((byte) 0x1A),

    /**
     * Notify indication dataset
     */
    NOTIFY_IND((byte) 0x1C),

    /**
     * DRIVEESANY	seen on Drive ES Starter with routing over S7
     */
    DRIVEESANY((byte) 0xA2),

    /**
     * Symbolic address mode of S7-1200
     */
    S_1200SYM((byte) 0xB2),

    /**
     * Kind of DB block read， seen only at an S7-400
     */
    DBREAD((byte) 0xB0),

    /**
     * Sinumerik NCK HMI access
     */
    NCK((byte) 0x82),
    ;

    private static final Map<Byte, ESyntaxID> map = new HashMap<>();

    static {
        for (ESyntaxID item : ESyntaxID.values()) {
            map.put(item.code, item);
        }
    }

    public static ESyntaxID from(byte data) {
        return map.get(data);
    }

    private byte code;

    private ESyntaxID(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}