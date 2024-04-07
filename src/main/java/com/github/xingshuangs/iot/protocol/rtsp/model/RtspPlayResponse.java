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
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRtpInfo;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspSessionInfo;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COMMA;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SESSION;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.RTP_INFO;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspPlayResponse extends RtspMessageResponse {

    /**
     * RTP相关信息
     * url=rtsp://192.17.1.63:554/trackID=1;seq=3658;rtptime=1710363406,url=rtsp://192.17.1.63:554/trackID=2;seq=6598;rtptime=4065225152
     */
    private List<RtspRtpInfo> rtpInfo = new ArrayList<>();

    /**
     * 特殊的会话信息
     */
    private RtspSessionInfo sessionInfo;

    public static RtspPlayResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("src is null or empty in RtspPlayResponse");
        }
        RtspPlayResponse response = new RtspPlayResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        if (map.containsKey(RTP_INFO)) {
            List<String> list = StringSpUtil.splitOneStepByLine(map.get(RTP_INFO).trim(), COMMA);
            response.rtpInfo = list.stream().map(RtspRtpInfo::fromString).collect(Collectors.toList());
        }
        // 会话ID
        if (map.containsKey(SESSION)) {
            response.sessionInfo = RtspSessionInfo.fromString(map.get(SESSION).trim());
            response.session = response.sessionInfo.getSessionId();
        }
        return response;
    }

}
