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

package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import lombok.Data;

/**
 * RTSP的会话部分
 * 814632351;timeout=60
 *
 * @author xingshuang
 */
@Data
public class RtspSessionInfo {

    /**
     * Session id.
     * 会话Id
     */
    private String sessionId;

    /**
     * Timeout millisecond.
     * 毫秒级的超时时间，实际是秒级，最终转换成毫秒级
     */
    private int timeout = 6000;

    public static RtspSessionInfo fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspSessionInfo, src is null or empty");
        }
        RtspSessionInfo sessionInfo = new RtspSessionInfo();
        int commaIndex = src.indexOf(";");
        if (commaIndex != -1) {
            sessionInfo.sessionId = src.substring(0, commaIndex);
        } else {
            sessionInfo.sessionId = src;
        }

        int equalIndex = src.indexOf("=");
        if (equalIndex != -1) {
            sessionInfo.timeout = Integer.parseInt(src.substring(equalIndex + 1)) * 1000;
        }
        return sessionInfo;
    }
}
