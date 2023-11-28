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
