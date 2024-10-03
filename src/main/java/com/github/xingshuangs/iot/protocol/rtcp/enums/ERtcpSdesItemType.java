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
 * SDES item type.
 * 资源项的类型
 *
 * @author xingshuang
 */
public enum ERtcpSdesItemType {

    /**
     * end of SDES list
     */
    END((byte) 0x00),

    /**
     * canonical name
     */
    CNAME((byte) 0x01),

    /**
     * username
     */
    NAME((byte) 0x02),

    /**
     * user's electronic mail address
     */
    EMAIL((byte) 0x03),

    /**
     * user's phone number
     */
    PHONE((byte) 0x04),

    /**
     * geographic user location
     */
    LOC((byte) 0x05),

    /**
     * name of application or tool
     */
    TOOL((byte) 0x06),

    /**
     * notice about the source
     */
    NOTE((byte) 0x07),

    /**
     * private extension
     */
    PRIV((byte) 0x08),
    ;

    private static Map<Byte, ERtcpSdesItemType> map;

    public static ERtcpSdesItemType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtcpSdesItemType item : ERtcpSdesItemType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    ERtcpSdesItemType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
