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

    private static Map<Byte, EParamVariableType> map;

    public static EParamVariableType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EParamVariableType item : EParamVariableType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EParamVariableType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
