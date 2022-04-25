package com.github.xingshuangs.iot.protocol.s7.enums;


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
     *
     */
    ACCESS_ERROR((byte) 0x87),
    ;

    private byte code;

    private EErrorClass(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
