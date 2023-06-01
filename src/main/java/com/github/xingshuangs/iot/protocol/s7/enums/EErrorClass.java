package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 错误类型
 *
 * @author xingshuang
 */
public enum EErrorClass {

    /**
     * 没有错误
     */
    NO_ERROR((byte) 0x00, "没有错误"),

    /**
     * 应用关系
     */
    APPLICATION_RELATIONSHIP((byte) 0x81, "应用关系"),

    /**
     * 对象定义
     */
    OBJECT_DEFINITION((byte) 0x82, "对象定义"),

    /**
     * 没有可用资源
     */
    NO_RESOURCES_AVAILABLE((byte) 0x83, "没有可用资源"),

    /**
     * 服务处理中错误
     */
    ERROR_ON_SERVICE_PROCESSING((byte) 0x84, "服务处理中错误"),

    /**
     * 请求错误
     */
    ERROR_ON_SUPPLIES((byte) 0x85, "请求错误"),

    /**
     * 访问错误
     */
    ACCESS_ERROR((byte) 0x87, "访问错误"),
    ;

    private static Map<Byte, EErrorClass> map;

    public static EErrorClass from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EErrorClass item : EErrorClass.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    EErrorClass(byte code, String description) {
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
