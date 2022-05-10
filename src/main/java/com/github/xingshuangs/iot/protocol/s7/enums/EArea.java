package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 数据的区域
 *
 * @author xingshuang
 */
public enum EArea {

    /**
     * 200系列系统信息
     */
    SYSTEM_INFO_OF_200_FAMILY((byte) 0x03),

    /**
     * family	200系列系统标志
     */
    SYSTEM_FLAGS_OF_200_FAMILY((byte) 0x05),

    /**
     * 200系列模拟量输入
     */
    ANALOG_INPUTS_OF_200_FAMILY((byte) 0x06),

    /**
     * 200系统模式量输出
     */
    ANALOG_OUTPUTS_OF_200_FAMILY((byte) 0x07),

    /**
     * 直接访问外设
     */
    DIRECT_PERIPHERAL_ACCESS((byte) 0x80),

    /**
     * 输入（I）
     */
    INPUTS((byte) 0x81),

    /**
     * 输出（Q）
     */
    OUTPUTS((byte) 0x82),

    /**
     * 内部标志（M）
     */
    FLAGS((byte) 0x83),

    /**
     * 数据块（DB）
     */
    DATA_BLOCKS((byte) 0x84),

    /**
     * 背景数据块（DI）
     */
    INSTANCE_DATA_BLOCKS((byte) 0x85),

    /**
     * 局部变量（L)
     */
    LOCAL_DATA((byte) 0x86),

    /**
     * 全局变量（V）
     */
    UNKNOWN_YET((byte) 0x87),

    /**
     * S7计数器（C）
     */
    S7_COUNTERS((byte) 0x1C),

    /**
     * S7定时器（T）
     */
    S7_TIMERS((byte) 0x1D),

    /**
     * IEC计数器（200系列）
     */
    IEC_COUNTERS((byte) 0x1E),

    /**
     * IEC定时器（200系列）
     */
    IEC_TIMERS((byte) 0x1F),

    ;

    private static final Map<Byte, EArea> map = new HashMap<>();

    static {
        for (EArea item : EArea.values()) {
            map.put(item.code, item);
        }
    }

    public static EArea from(byte data) {
        return map.get(data);
    }

    private byte code;

    EArea(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
