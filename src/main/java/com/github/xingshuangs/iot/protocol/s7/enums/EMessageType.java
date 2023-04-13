package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Message Type： 消息的一般类型（有时称为ROSCTR类型），消息的其余部分在很大程度上取决于Message Type和功能代码。
 *
 * @author xingshuang
 */
public enum EMessageType {

    /**
     * 开工干活的意思，主设备通过job向从设备发出“干活”的命令，具体是读取数据还是写数据由parameter决定
     */
    JOB((byte) 0x01),

    /**
     * 确认 确认有没有数据字段
     */
    ACK((byte) 0x02),

    /**
     * 从设备回应主设备的job
     */
    ACK_DATA((byte) 0x03),

    /**
     * 原始协议的扩展，参数字段包含请求/响应id，（用于编程/调试，SZL读取，安全功能，时间设置，循环读取…）
     */
    USER_DATA((byte) 0x07),
    ;

    private static Map<Byte, EMessageType> map;

    public static EMessageType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMessageType item : EMessageType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EMessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
