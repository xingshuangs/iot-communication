package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspContentType;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_LENGTH;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_TYPE;

/**
 * GetParameter请求
 *
 * @author xingshuang
 */
@Getter
public class RtspSetParameterRequest extends RtspMessageRequest {

    private final Map<String, String> parameterMap = new LinkedHashMap<>();

    public RtspSetParameterRequest(URI uri, String session) {
        super(ERtspMethod.SET_PARAMETER, uri, session);
    }

    public RtspSetParameterRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        super(ERtspMethod.SET_PARAMETER, uri, session, authenticator);
    }

    public void addParameter(String name, String value) {
        this.parameterMap.put(name, value);
    }

    public void removeParameter(String name) {
        this.parameterMap.remove(name);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameterMap);
    }

    @Override
    protected void addEntityHeader(StringBuilder sb) {
        if (!this.parameterMap.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            this.parameterMap.forEach((key, value) -> stringBuilder.append(key).append(COLON + SP).append(value).append(CRLF));
            String content = stringBuilder.toString();

            sb.append(CONTENT_TYPE).append(COLON + SP).append(ERtspContentType.PARAMETER.getCode()).append(CRLF);
            sb.append(CONTENT_LENGTH).append(COLON + SP).append(content.length()).append(CRLF);
        }
    }

    @Override
    protected void addMessageBody(StringBuilder sb) {
        if (!this.parameterMap.isEmpty()) {
            this.parameterMap.forEach((key, value) -> sb.append(key).append(COLON + SP).append(value).append(CRLF));
        }
    }
}
