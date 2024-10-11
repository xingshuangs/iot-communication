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
 * Message Type： 消息的一般类型（有时称为ROSCTR类型），消息的其余部分在很大程度上取决于Message Type和功能代码。
 *
 * @author xingshuang
 */
public enum EMessageType {

    /**
     * Start work.
     * 开工干活的意思，主设备通过job向从设备发出“干活”的命令，具体是读取数据还是写数据由parameter决定
     */
    JOB((byte) 0x01),

    /**
     * Confirm Confirm that there are no data fields.
     * 确认 确认有没有数据字段
     */
    ACK((byte) 0x02),

    /**
     * The slave device responds to the job of the master device.
     * 从设备回应主设备的job
     */
    ACK_DATA((byte) 0x03),

    /**
     * An extension of the original protocol.
     * 原始协议的扩展，参数字段包含请求/响应id，（用于编程/调试，SZL读取，安全功能，时间设置，循环读取…）
     */
    USER_DATA((byte) 0x07),
    ;

    private static Map<Byte, EMessageType> map;

    public static EMessageType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMessageType item : EMessageType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EMessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
