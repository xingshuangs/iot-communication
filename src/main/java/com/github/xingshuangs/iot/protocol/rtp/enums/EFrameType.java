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
 * Frame type.
 * (帧的类别)
 *
 * @author xingshuang
 */
public enum EFrameType {
    /**
     * Problem.
     * (问题)
     */
    PROBLEM((byte) 0x00),

    /**
     * Video.
     * (视频)
     */
    VIDEO((byte) 0x01),

    /**
     * Audio.
     * (音频)
     */
    AUDIO((byte) 0x02),

    ;

    private static Map<Byte, EFrameType> map;

    public static EFrameType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EFrameType item : EFrameType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EFrameType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
