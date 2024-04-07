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

package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 源
 * - 1517245007527432 1517245007527432 IN IP4 10.3.8.202
 *
 * @author xingshuang
 */
@Data
public class RtspSdpOrigin {
    /**
     * 会话ID
     */
    private String username;

    /**
     *
     */
    private String sessionId;

    /**
     * 会话版本
     */
    private String sessionVersion;

    /**
     * 网络类型
     */
    private String networkType;

    /**
     * 地址类型
     */
    private String addressType;

    /**
     * 单播地址
     */
    private String unicastAddress;

    public static RtspSdpOrigin fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("The SDP fails to parse Origin data sources");
        }

        RtspSdpOrigin origin = new RtspSdpOrigin();
        String[] split = src.split(SP);
        if (split.length != 6) {
            throw new RtspCommException("RtspSdpOrigin data is incorrect and cannot be parsed");
        }
        origin.username = split[0];
        origin.sessionId = split[1];
        origin.sessionVersion = split[2];
        origin.networkType = split[3];
        origin.addressType = split[4];
        origin.unicastAddress = split[5];
        return origin;
    }
}
