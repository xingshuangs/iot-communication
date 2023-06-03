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

    STRING("string", 1),

    /**
     * 目前只对S7协议使用，对应的JAVA类型long
     */
    TIME("time", 4),

    /**
     * 目前只对S7协议使用，对应的JAVA类型LocalDate
     */
    DATE("date", 2),

    /**
     * 目前只对S7协议使用，对应的JAVA类型LocalTime
     */
    TIME_OF_DAY("timeOfDay", 4),

    /**
     * 目前只对S7协议使用，对应的JAVA类型LocalDateTime
     */
    DTL("dtl", 12),

    ;

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
