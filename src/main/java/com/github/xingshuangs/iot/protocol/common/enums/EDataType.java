package com.github.xingshuangs.iot.protocol.common.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 数据类型
 *
 * @author xingshuang
 */
public enum EDataType {

    BOOL("bool", 1),

    BYTE("byte", 1),

    UINT16("uint16", 2),

    INT16("int16", 2),

    UINT32("uint32", 4),

    INT32("int32", 4),

    FLOAT32("float32", 4),

    FLOAT64("float64", 8),

    // S7协议字符串比较特殊，暂不实现
    STRING("string", 1);

//    UNKNOWN("unknown", 0);

    private static Map<String, EDataType> map;

    public static EDataType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EDataType item : EDataType.values()) {
                map.put(item.name, item);
            }
        }
        return map.get(data);
    }

    /**
     * 字节长度
     */
    private final int byteLength;

    /**
     * 名称
     */
    private final String name;

    EDataType(String name, int byteLength) {
        this.name = name;
        this.byteLength = byteLength;
    }

    public int getByteLength() {
        return byteLength;
    }

    public String getName() {
        return name;
    }
}
