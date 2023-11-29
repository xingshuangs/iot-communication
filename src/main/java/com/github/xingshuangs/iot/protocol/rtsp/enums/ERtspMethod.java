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

package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * ERtspMethod方法
 *
 * @author xingshuang
 */
public enum ERtspMethod {

    /**
     * 广播
     */
    ANNOUNCE("ANNOUNCE"),

    /**
     * 重定向
     */
    REDIRECT("REDIRECT"),


    /**
     * 选项
     */
    OPTIONS("OPTIONS"),

    /**
     * 描述
     */
    DESCRIBE("DESCRIBE"),

    /**
     * 设置
     */
    SETUP("SETUP"),

    /**
     * 暂停
     */
    PAUSE("PAUSE"),

    /**
     * 播放
     */
    PLAY("PLAY"),

    /**
     * 录入
     */
    RECORD("RECORD"),

    /**
     * 销毁
     */
    TEARDOWN("TEARDOWN"),

    /**
     * 获取参数
     */
    GET_PARAMETER("GET_PARAMETER"),

    /**
     * 设置参数
     */
    SET_PARAMETER("SET_PARAMETER"),

    ;

    private static Map<String, ERtspMethod> map;

    public static ERtspMethod from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspMethod item : ERtspMethod.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    ERtspMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
