package com.github.xingshuangs.iot.protocol.modbus.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 异常码枚举
 *
 * @author xingshuang
 */
public enum EMbExceptionCode {

    /**
     * 非法功能码
     */
    ILLEGAL_FUNCTION((byte) 0x01, "非法功能码"),

    /**
     * 非法数据地址
     */
    ILLEGAL_DATA_ADDRESS((byte) 0x02, "非法数据地址"),

    /**
     * 非法数据值
     */
    ILLEGAL_DATA_VALUE((byte) 0x03, "非法数据值"),

    /**
     * 从站设备失败
     */
    SLAVE_DEVICE_FAILURE((byte) 0x04, "从站设备失败"),

    ;

    private static Map<Byte, EMbExceptionCode> map;

    public static EMbExceptionCode from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMbExceptionCode item : EMbExceptionCode.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    EMbExceptionCode(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
