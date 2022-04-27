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
    NO_ERROR((byte) 0x00),

    /**
     * 应用关系
     */
    APPLICATION_RELATIONSHIP((byte) 0x81),

    /**
     * 对象定义
     */
    OBJECT_DEFINITION((byte) 0x82),

    /**
     * 没有可用资源
     */
    NO_RESOURCES_AVAILABLE((byte) 0x83),

    /**
     * 服务处理中错误
     */
    ERROR_ON_SERVICE_PROCESSING((byte) 0x84),

    /**
     * 请求错误
     */
    ERROR_ON_SUPPLIES((byte) 0x85),

    /**
     * 访问错误
     */
    ACCESS_ERROR((byte) 0x87),
    ;

    private static final Map<Byte, EErrorClass> map = new HashMap<>();

    static {
        for (EErrorClass item : EErrorClass.values()) {
            map.put(item.code, item);
        }
    }

    public static EErrorClass from(byte data) {
        return map.get(data);
    }

    private byte code;

    private EErrorClass(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
