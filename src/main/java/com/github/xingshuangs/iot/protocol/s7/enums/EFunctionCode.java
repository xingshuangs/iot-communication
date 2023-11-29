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
 * 功能码 Job request/Ack-Data function codes
 *
 * @author xingshuang
 */
public enum EFunctionCode {
    /**
     * CPU服务
     */
    CPU_SERVICES((byte) 0x00),

    /**
     * 读变量
     */
    READ_VARIABLE((byte) 0x04),

    /**
     * 写变量
     */
    WRITE_VARIABLE((byte) 0x05),

    /**
     * 开始下载
     */
//    START_DOWNLOAD((byte) 0x1A),
    START_DOWNLOAD((byte) 0xFA),

    /**
     * 下载阻塞
     */
//    DOWNLOAD((byte) 0x1B),
    DOWNLOAD((byte) 0xFB),

    /**
     * 下载结束
     */
//    END_DOWNLOAD((byte) 0x1C),
    END_DOWNLOAD((byte) 0xFC),

    /**
     * 开始上传
     */
    START_UPLOAD((byte) 0x1D),

    /**
     * 上传
     */
    UPLOAD((byte) 0x1E),

    /**
     * 结束上传
     */
    END_UPLOAD((byte) 0x1F),

    /**
     * 控制PLC
     */
    PLC_CONTROL((byte) 0x28),

    /**
     * 停止PLC
     */
    PLC_STOP((byte) 0x29),

    /**
     * 设置通信
     */
    SETUP_COMMUNICATION((byte) 0xF0),
    ;

    private static Map<Byte, EFunctionCode> map;

    public static EFunctionCode from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EFunctionCode item : EFunctionCode.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    EFunctionCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
