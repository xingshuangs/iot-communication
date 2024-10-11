/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Data area.
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

    private static Map<Byte, EArea> map;

    public static EArea from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EArea item : EArea.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EArea(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
