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
 * 目标文件系统
 *
 * @author xingshuang
 */
public enum EDestinationFileSystem {
    /**
     * 对应0x50，（Passive (copied, but not chained) module)：被动文件系统
     */
    P((byte) 0x50),

    /**
     * 对应0x41，(Active embedded module)：主动文件系统
     */
    A((byte) 0x41),

    /**
     * 对应0x42，(Active as well as passive module)：既主既被文件系统种
     */
    B((byte) 0x42),
    ;

    private static Map<Byte, EDestinationFileSystem> map;

    public static EDestinationFileSystem from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EDestinationFileSystem item : EDestinationFileSystem.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EDestinationFileSystem(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
