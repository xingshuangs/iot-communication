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


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Session info.
 * 会话
 *
 * @author xingshuang
 */
@Data
public class RtspSdpSession {

    /**
     * Version
     * 版本 v（必选） 0
     */
    private Integer version;

    /**
     * Origin.
     * 源 o（必选） - 1517245007527432 1517245007527432 IN IP4 10.3.8.202
     */
    private RtspSdpOrigin origin;

    /**
     * Session name.
     * 会话名称 s（必选） Media Presentation
     */
    private String sessionName;

    /**
     * Time
     * 时间（必选）0 0
     */
    private RtspSdpTime time;

    /**
     * Session info.
     * 会话信息(可选)
     */
    private String sessionInfo;

    /**
     * Uri
     * 统一资源描述(可选)
     */
    private String uri;

    /**
     * Email.
     * 电子邮件(可选)
     */
    private String email;

    /**
     * Phone.
     * 电话号码(可选)
     */
    private String phone;

    /**
     * Connection.
     * 连接信息(可选) IN IP4 0.0.0.0
     */
    private RtspSdpConnection connection;

    /**
     * Bandwidth.
     * 带宽(可选) AS:5050
     */
    private RtspSdpBandwidth bandwidth;

    /**
     * Echo time.
     * 重复时间(可选)
     */
    private RtspSdpEchoTime echoTime;

    /**
     * Attributes.
     * 附加信息(可选)
     */
    private Map<String, String> attributes = new HashMap<>();

    public static RtspSdpSession fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("session, src is null or empty");
        }
        RtspSdpSession session = new RtspSdpSession();
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, CRLF, EQUAL);

        map.forEach((key, value) -> {
            switch (key) {
                case "v":
                    session.version = Integer.parseInt(value);
                    break;
                case "o":
                    session.origin = RtspSdpOrigin.fromString(value);
                    break;
                case "s":
                    session.sessionName = value;
                    break;
                case "c":
                    session.connection = RtspSdpConnection.fromString(value);
                    break;
                case "e":
                    session.email = value;
                    break;
                case "b":
                    session.bandwidth = RtspSdpBandwidth.fromString(value);
                    break;
                case "t":
                    session.time = RtspSdpTime.fromString(value);
                    break;
                case "a":
                    int index = value.indexOf(COLON);
                    if (index == -1) {
                        session.attributes.put(value, "");
                    } else {
                        session.attributes.put(value.substring(0, index), value.substring(index + 1));
                    }
                    break;
                default:
                    break;
            }
        });
        return session;
    }
}
