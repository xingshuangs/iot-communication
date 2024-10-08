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

package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspSessionInfo;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspTransport;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Setup response
 *
 * @author xingshuang
 */
@Getter
public final class RtspSetupResponse extends RtspMessageResponse {

    /**
     * Transport.
     * (通道)
     */
    private RtspTransport transport;

    /**
     * Session info.
     * (特殊的会话信息)
     */
    private RtspSessionInfo sessionInfo;

    public static RtspSetupResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("src is null or empty in RtspSetupResponse");
        }
        RtspSetupResponse response = new RtspSetupResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        if (map.containsKey(SESSION)) {
            response.sessionInfo = RtspSessionInfo.fromString(map.get(SESSION).trim());
            response.session = response.sessionInfo.getSessionId();
        }
        if (map.containsKey(TRANSPORT)) {
            response.transport = RtspTransport.fromString(map.get(TRANSPORT).trim());
        }
        return response;
    }
}
