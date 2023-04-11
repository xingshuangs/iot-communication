package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.*;
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
    private final ERtspMethod method;

    /**
     * 代理用户
     */
    public static final String USER_AGENT_VALUE = "IOT-COMMUNICATION";

    /**
     * 资源地址
     */
    private final URI uri;

    public RtspMessageRequest(ERtspMethod method, URI uri) {
        this.method = method;
        this.uri = uri;
        this.cSeq = RtspMessage.getUint16Number();
    }

    public RtspMessageRequest(String version, Map<String, String> headers, ERtspMethod method, URI uri) {
        super(version, headers);
        this.method = method;
        this.uri = uri;
        this.cSeq = RtspMessage.getUint16Number();
    }

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        // Request-Line = Method SP Request-URI SP RTSP-Version CRLF
        sb.append(this.method.getCode()).append(SP).append(this.uri.toString()).append(SP).append(this.version).append(CRLF);
        // CSeq: 1
        sb.append(C_SEQ).append(COLON).append(this.cSeq).append(CRLF);
        sb.append(USER_AGENT).append(COLON).append(USER_AGENT_VALUE).append(CRLF);
        // 请求头
        this.headers.forEach((key, value) -> sb.append(key).append(COLON).append(value).append(CRLF));
        return sb.toString();
    }
}
