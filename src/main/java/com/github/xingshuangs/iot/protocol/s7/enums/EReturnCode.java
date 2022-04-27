package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 操作的返回值，0xff信号成功。在“ 写入请求”消息中，此字段始终设置为零。
 *
 * @author xingshuang
 */
public enum EReturnCode {

    /**
     * 未定义，预留
     */
    RESERVED((byte) 0x00),

    /**
     * 硬件错误
     */
    HARDWARE_ERROR((byte) 0x01),

    /**
     * 对象不允许访问
     */
    ACCESSING_THE_OBJECT_NOT_ALLOWED((byte) 0x03),

    /**
     * 无效地址，所需的地址超出此PLC的极限
     */
    INVALID_ADDRESS((byte) 0x05),

    /**
     * 数据类型不支持
     */
    DATA_TYPE_NOT_SUPPORTED((byte) 0x06),

    /**
     * 日期类型不一致
     */
    DATA_TYPE_INCONSISTENT((byte) 0x07),

    /**
     * 对象不存在
     */
    OBJECT_DOES_NOT_EXIST((byte) 0x0A),

    /**
     * 成功
     */
    SUCCESS((byte) 0xFF),

    ;

    private static final Map<Byte, EReturnCode> map = new HashMap<>();

    static {
        for (EReturnCode item : EReturnCode.values()) {
            map.put(item.code, item);
        }
    }

    public static EReturnCode from(byte data) {
        return map.get(data);
    }

    private byte code;

    private EReturnCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

}
