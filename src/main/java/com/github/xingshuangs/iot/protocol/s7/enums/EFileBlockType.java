package com.github.xingshuangs.iot.protocol.s7.enums;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件地址块类型
 *
 * @author xingshuang
 */
public enum EFileBlockType {
    /**
     * 对应0x3038
     */
    OB("08"),

    /**
     * 对应0x3039
     */
    CMOD("09"),

    /**
     * 对应0x3041
     */
    DB("0A"),

    /**
     * 对应0x3042
     */
    SDB("0B"),

    /**
     * 对应0x3043
     */
    FC("0C"),

    /**
     * 对应0x3044
     */
    SFC("0D"),

    /**
     * 对应0x3045
     */
    FB("0E"),

    /**
     * 对应0x3046
     */
    SFB("0F"),
    ;

    private static Map<String, EFileBlockType> map;

    public static EFileBlockType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EFileBlockType item : EFileBlockType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    private final byte[] byteArray;

    EFileBlockType(String code) {
        this.code = code;
        this.byteArray = code.getBytes(StandardCharsets.US_ASCII);
    }

    public String getCode() {
        return code;
    }

    public byte[] getByteArray() {
        return byteArray;
    }
}
