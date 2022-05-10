package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Transport size (variable Type) in Item data
 *
 * @author xingshuang
 */
public enum EParamVariableType {

    /**
     * 位
     */
    BIT((byte) 0x01),

    /**
     * 字节
     */
    BYTE((byte) 0x02),

    /**
     * 字符
     */
    CHAR((byte) 0x03),

    /**
     * 字
     */
    WORD((byte) 0x04),

    /**
     * int
     */
    INT((byte) 0x05),

    /**
     * 双字
     */
    DWORD((byte) 0x06),

    /**
     * DINT
     */
    DINT((byte) 0x07),

    /**
     * 浮点
     */
    REAL((byte) 0x08),

    /**
     * 日期
     */
    DATE((byte) 0x09),

    /**
     * TOD
     */
    TOD((byte) 0x0A),

    /**
     * 时间
     */
    TIME((byte) 0x0B),

    /**
     * S5TIME
     */
    S5TIME((byte) 0x0C),

    /**
     * 日期和时间
     */
    DATE_AND_TIME((byte) 0x0F),

    /**
     * 计数器
     */
    COUNTER((byte) 0x1C),

    /**
     * 定时器
     */
    TIMER((byte) 0x1D),

    /**
     * IEC_TIMER
     */
    IEC_TIMER((byte) 0x1E),

    /**
     * IEC_COUNTER
     */
    IEC_COUNTER((byte) 0x1F),

    /**
     * HS_COUNTER
     */
    HS_COUNTER((byte) 0x20),
    ;

    private static final Map<Byte, EParamVariableType> map = new HashMap<>();

    static {
        for (EParamVariableType item : EParamVariableType.values()) {
            map.put(item.code, item);
        }
    }

    public static EParamVariableType from(byte data) {
        return map.get(data);
    }

    private byte code;

    EParamVariableType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
