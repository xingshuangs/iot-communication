package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 目标文件系统
 *
 * @author xingshuang
 */
public enum EDestinationFileSystem {
    /**
     * 对应0x50，（Passive (copied, but not chained) module)：被动文件系统
     */
    P((byte) 0x50),

    /**
     * 对应0x41，(Active embedded module)：主动文件系统
     */
    A((byte) 0x41),

    /**
     * 对应0x42，(Active as well as passive module)：既主既被文件系统种
     */
    B((byte) 0x42),
    ;

    private static Map<Byte, EDestinationFileSystem> map;

    public static EDestinationFileSystem from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EDestinationFileSystem item : EDestinationFileSystem.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EDestinationFileSystem(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
