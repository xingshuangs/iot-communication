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
 * Data variable type of request data.
 * 数据返回Transport size in data Transport size (variable Type) 变量的类型和长度
 *
 * @author xingshuang
 */
public enum EDataVariableType {

    /**
     * 无
     */
    NULL((byte) 0x00),

    /**
     * bit access, len is in bits
     */
    BIT((byte) 0x03),

    /**
     * byte/word/dword access, len is in bits
     */
    BYTE_WORD_DWORD((byte) 0x04),

    /**
     * integer access, len is in bits
     */
    INTEGER((byte) 0x05),

    /**
     * integer access, len is in bytes
     */
    DINTEGER((byte) 0x06),

    /**
     * real access, len is in bytes
     */
    REAL((byte) 0x07),

    /**
     * octet string, len is in bytes
     */
    OCTET_STRING((byte) 0x09),
    ;

    private static Map<Byte, EDataVariableType> map;

    public static EDataVariableType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EDataVariableType item : EDataVariableType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EDataVariableType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
