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
 * 寻址模式和项结构其余部分的格式，它具有任意类型寻址的常量值0x10
 *
 * @author xingshuang
 */
public enum ESyntaxID {
    /**
     * Address data S7-Any pointer-like   DB1.DBX10.2
     */
    S7ANY((byte) 0x10),

    /**
     * R_ID for PBC
     */
    PBC_R_ID((byte) 0x13),

    /**
     * Alarm lock/free dataset
     */
    ALARM_LOCKFREE((byte) 0x15),

    /**
     * Alarm indication dataset
     */
    ALARM_IND((byte) 0x16),

    /**
     * Alarm acknowledge message dataset
     */
    ALARM_ACK((byte) 0x19),

    /**
     * Alarm query request dataset
     */
    ALARM_QUERYREQ((byte) 0x1A),

    /**
     * Notify indication dataset
     */
    NOTIFY_IND((byte) 0x1C),

    /**
     * DRIVEESANY	seen on Drive ES Starter with routing over S7
     */
    DRIVEESANY((byte) 0xA2),

    /**
     * Symbolic byteAddress mode of S7-1200
     */
    S_1200SYM((byte) 0xB2),

    /**
     * Kind of DB block read， seen only at an S7-400
     */
    DBREAD((byte) 0xB0),

    /**
     * Sinumerik NCK HMI access
     */
    NCK((byte) 0x82),
    ;

    private static Map<Byte, ESyntaxID> map;

    public static ESyntaxID from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ESyntaxID item : ESyntaxID.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    ESyntaxID(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
