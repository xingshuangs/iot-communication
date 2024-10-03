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

package com.github.xingshuangs.iot.protocol.rtcp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Package type class.
 * 数据包的类型
 *
 * @author xingshuang
 */
public enum ERtcpPackageType {

    /**
     * Send report.
     * 发送端报告,200
     */
    SR((byte) 0xC8),

    /**
     * Receive report.
     * 接收端报告,201
     */
    RR((byte) 0xC9),

    /**
     * Source description.
     * 源点描述,202
     */
    SDES((byte) 0xCA),

    /**
     * End transport.
     * 结束传输,203
     */
    BYE((byte) 0xCB),

    /**
     * App
     * 特定应用,204
     */
    APP((byte) 0xCC),
    ;

    private static Map<Byte, ERtcpPackageType> map;

    public static ERtcpPackageType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtcpPackageType item : ERtcpPackageType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    ERtcpPackageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
