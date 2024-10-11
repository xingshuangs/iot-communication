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

package com.github.xingshuangs.iot.common.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Basic data type.
 * (数据类型)
 *
 * @author xingshuang
 */
public enum EDataType {

    BOOL("bool", 1),

    BYTE("byte", 1),

    UINT16("uint16", 2),

    INT16("int16", 2),

    UINT32("uint32", 4),

    INT32("int32", 4),

    INT64("int64", 8),

    FLOAT32("float32", 4),

    FLOAT64("float64", 8),

    STRING("string", 1),

    /**
     *  It is used only for the S7 protocol currently, and the corresponding JAVA type is long.
     * (目前只对S7协议使用，对应的JAVA类型long)
     */
    TIME("time", 4),

    /**
     * It is used only for the S7 protocol currently, and the corresponding JAVA type is LocalDate.
     * (目前只对S7协议使用，对应的JAVA类型LocalDate)
     */
    DATE("date", 2),

    /**
     * It is used only for the S7 protocol currently, and the corresponding JAVA type is LocalTime.
     * (目前只对S7协议使用，对应的JAVA类型LocalTime)
     */
    TIME_OF_DAY("timeOfDay", 4),

    /**
     * It is used only for the S7 protocol currently, and the corresponding JAVA type is LocalDateTime.
     * (目前只对S7协议使用，对应的JAVA类型LocalDateTime)
     */
    DTL("dtl", 12),

    ;

//    UNKNOWN("unknown", 0);

    private static Map<String, EDataType> map;

    public static EDataType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EDataType item : EDataType.values()) {
                map.put(item.name, item);
            }
        }
        return map.get(data);
    }

    /**
     * Data byte array length.
     * (字节长度)
     */
    private final int byteLength;

    /**
     * Data name.
     * (名称)
     */
    private final String name;

    EDataType(String name, int byteLength) {
        this.name = name;
        this.byteLength = byteLength;
    }

    public int getByteLength() {
        return byteLength;
    }

    public String getName() {
        return name;
    }
}
