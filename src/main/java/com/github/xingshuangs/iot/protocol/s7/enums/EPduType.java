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
 * PDU类型（CRConnect Request 连接请求）
 *
 * @author xingshuang
 */
public enum EPduType {

    /**
     * 连接请求
     */
    CONNECT_REQUEST((byte) 0xE0),

    /**
     * 连接确认
     */
    CONNECT_CONFIRM((byte) 0xD0),

    /**
     * 断开请求
     */
    DISCONNECT_REQUEST((byte) 0x80),

    /**
     * 断开确认
     */
    DISCONNECT_CONFIRM((byte) 0xC0),

    /**
     * 拒绝
     */
    REJECT((byte) 0x50),

    /**
     * 数据
     */
    DT_DATA((byte) 0xF0),
    ;

    private static Map<Byte, EPduType> map;

    public static EPduType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EPduType item : EPduType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EPduType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
