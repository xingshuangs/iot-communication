package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

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
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, -1, null);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, int session) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, session, null);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, AbstractAuthenticator authenticator) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, -1, authenticator);
    }

    public RtspMessageRequest(ERtspMethod method, URI uri, int session, AbstractAuthenticator authenticator) {
        this(RtspMessage.VERSION_1_0, new HashMap<>(), method, uri, session, authenticator);
    }

    public RtspMessageRequest(String version, Map<String, String> headers, ERtspMethod method, URI uri, int session, AbstractAuthenticator authenticator) {
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
        // Request-Line = Method SP Request-URI SP RTSP-Version CRLF
        sb.append(this.method.getCode()).append(SP).append(this.uri.toString()).append(SP).append(this.version).append(CRLF);
        // CSeq: 1
        sb.append(C_SEQ).append(COLON).append(this.cSeq).append(CRLF);
        // authorization
        if (this.authenticator != null) {
            sb.append(AUTHORIZATION).append(COLON).append(this.authenticator.createResponse()).append(CRLF);
        }
        sb.append(USER_AGENT).append(COLON).append(USER_AGENT_VALUE).append(CRLF);
        // session
        if (session >= 0) {
            sb.append(SESSION).append(COLON).append(this.session).append(CRLF);
        }
        // 请求头
        this.headers.forEach((key, value) -> sb.append(key).append(COLON).append(value).append(CRLF));
        return sb.toString();
    }
}
