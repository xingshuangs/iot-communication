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


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件地址块类型
 *
 * @author xingshuang
 */
public enum EFileBlockType {
    /**
     * 对应0x3038
     */
    OB("08"),

    /**
     * 对应0x3039
     */
    CMOD("09"),

    /**
     * 对应0x3041
     */
    DB("0A"),

    /**
     * 对应0x3042
     */
    SDB("0B"),

    /**
     * 对应0x3043
     */
    FC("0C"),

    /**
     * 对应0x3044
     */
    SFC("0D"),

    /**
     * 对应0x3045
     */
    FB("0E"),

    /**
     * 对应0x3046
     */
    SFB("0F"),
    ;

    private static Map<String, EFileBlockType> map;

    public static EFileBlockType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EFileBlockType item : EFileBlockType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    private final byte[] byteArray;

    EFileBlockType(String code) {
        this.code = code;
        this.byteArray = code.getBytes(StandardCharsets.US_ASCII);
    }

    public String getCode() {
        return code;
    }

    public byte[] getByteArray() {
        return byteArray;
    }
}
