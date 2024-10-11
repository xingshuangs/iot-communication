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
 * Error class.
 * (错误类型)
 *
 * @author xingshuang
 */
public enum EErrorClass {

    /**
     * no error.
     * 没有错误
     */
    NO_ERROR((byte) 0x00, "no error"),

    /**
     * application relationship.
     * 应用关系
     */
    APPLICATION_RELATIONSHIP((byte) 0x81, "application relationship"),

    /**
     * object definition.
     * 对象定义
     */
    OBJECT_DEFINITION((byte) 0x82, "object definition"),

    /**
     * no resources available.
     * 没有可用资源
     */
    NO_RESOURCES_AVAILABLE((byte) 0x83, "no resources available"),

    /**
     * error on service processing.
     * 服务处理中错误
     */
    ERROR_ON_SERVICE_PROCESSING((byte) 0x84, "error on service processing"),

    /**
     * error on supplies.
     * 请求错误
     */
    ERROR_ON_SUPPLIES((byte) 0x85, "error on supplies"),

    /**
     * access error.
     * 访问错误
     */
    ACCESS_ERROR((byte) 0x87, "access error"),

    /**
     * download error.
     * 下载错误
     */
    DOWNLOAD_ERROR((byte) 0xD2, "download error"),
    ;

    private static Map<Byte, EErrorClass> map;

    public static EErrorClass from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EErrorClass item : EErrorClass.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    EErrorClass(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
