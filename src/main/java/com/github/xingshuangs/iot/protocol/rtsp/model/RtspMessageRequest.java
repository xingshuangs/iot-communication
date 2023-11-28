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


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.AUTHORIZATION;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.USER_AGENT;


/**
 * RTSP消息请求
 *
 * @author xingshuang
 */
@Getter
@Setter
public class RtspMessageRequest extends RtspMessage {
    /**
     * 方法
     */
    protected final ERtspMethod method;

    /**
     * 代理用户
     */
    public static final String USER_AGENT_VALUE = "IOT-COMMUNICATION";

    /**
     * 资源地址
     */
    protected final URI uri;

    /**
     * 授权
     */
    protected AbstractAuthenticator authenticator;

    public RtspMessageRequest(ERtspMethod method, URI uri) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, "", null);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, String session) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, session, null);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, AbstractAuthenticator authenticator) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, "", authenticator);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, String session, AbstractAuthenticator authenticator) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, session, authenticator);
    }

    public RtspMessageRequest(String version, Map<String, String> headers, ERtspMethod method, URI uri, String session, AbstractAuthenticator authenticator) {
        super(version, headers);
        this.method = method;
        this.uri = uri;
        this.session = session;
        this.authenticator = authenticator;
        this.cSeq = RtspMessage.getUint16Number();
    }

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        this.addRequestLine(sb);
        this.addGeneralHeader(sb);
        this.addCommonRequestHeader(sb);
        this.addRequestHeader(sb);
        this.addEntityHeader(sb);
        sb.append(CRLF);
        this.addMessageBody(sb);
        return sb.toString();
    }

    private void addRequestLine(StringBuilder sb) {
        // Request-Line = Method SP Request-URI SP RTSP-Version CRLF
        sb.append(this.method.getCode()).append(SP).append(this.uri.toString()).append(SP).append(this.version).append(CRLF);
    }

    private void addGeneralHeader(StringBuilder sb) {
        // CSeq: 1
        sb.append(C_SEQ).append(COLON + SP).append(this.cSeq).append(CRLF);
    }

    private void addCommonRequestHeader(StringBuilder sb) {
        // authorization
        if (this.authenticator != null) {
            sb.append(AUTHORIZATION).append(COLON + SP).append(this.authenticator.createResponse()).append(CRLF);
        }
        sb.append(USER_AGENT).append(COLON + SP).append(USER_AGENT_VALUE).append(CRLF);
        // session
        if (this.session != null && !this.session.equals("")) {
            sb.append(SESSION).append(COLON + SP).append(this.session).append(CRLF);
        }
        // 请求头
        this.headers.forEach((key, value) -> sb.append(key).append(COLON + SP).append(value).append(CRLF));
    }

    protected void addRequestHeader(StringBuilder sb) {
        // NOOP
    }

    protected void addEntityHeader(StringBuilder sb) {
        // NOOP
    }

    protected void addMessageBody(StringBuilder sb) {
        // NOOP
    }
}
