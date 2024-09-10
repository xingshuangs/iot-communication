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

package com.github.xingshuangs.iot.protocol.rtp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * H264的Slice类型
 *
 * @author xingshuang
 */
public enum EH264SliceType {

    /**
     * P帧
     */
    P(0),

    /**
     * B帧
     */
    B(1),

    /**
     * I帧
     */
    I(2),

    /**
     * SP帧
     */
    SP(3),

    /**
     * SI帧
     */
    SI(4),

    ;

    private static Map<Integer, EH264SliceType> map;

    public static EH264SliceType from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (EH264SliceType item : EH264SliceType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    EH264SliceType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
