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

package com.github.xingshuangs.iot.protocol.melsec.enums;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 帧类型
 *
 * @author xingshuang
 */
@Getter
public enum EMcFrameType {

    /**
     * 4E
     */
    FRAME_4E("4E", 0x0054, 0x00D4),

    /**
     * 3E
     */
    FRAME_3E("3E", 0x0050, 0x00D0),

    /**
     * 1E，副帧头就是指令类型，这里先写个临时的数据
     */
    FRAME_1E("1E", 0x00, 0x00),
    ;

    private static Map<String, EMcFrameType> map;

    public static EMcFrameType from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcFrameType item : EMcFrameType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    /**
     * 类型码
     */
    private final String code;

    /**
     * 请求副帧头
     */
    private final int reqSubHeader;

    /**
     * 响应副帧头
     */
    private final int ackSubHeader;

    EMcFrameType(String code, int reqSubHeader, int ackSubHeader) {
        this.code = code;
        this.reqSubHeader = reqSubHeader;
        this.ackSubHeader = ackSubHeader;
    }


}
