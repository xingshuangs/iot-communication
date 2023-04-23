package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspContentType;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
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
public final class RtspGetParameterRequest extends RtspMessageRequest {

    /**
     * 有序set集合，参数名
     */
    private final Set<String> parameterNames = new LinkedHashSet<>();

    public RtspGetParameterRequest(URI uri, String session) {
        super(ERtspMethod.GET_PARAMETER, uri, session);
    }

    public RtspGetParameterRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        super(ERtspMethod.GET_PARAMETER, uri, session, authenticator);
    }

    public void addParameter(String name) {
        this.parameterNames.add(name);
    }

    public void removeParameter(String name) {
        this.parameterNames.remove(name);
    }

    public Set<String> getParameters() {
        return Collections.unmodifiableSet(this.parameterNames);
    }

    @Override
    protected void addEntityHeader(StringBuilder sb) {
        if (!this.parameterNames.isEmpty()) {
            String names = String.join(CRLF, this.parameterNames) + CRLF;
            sb.append(CONTENT_TYPE).append(COLON + SP).append(ERtspContentType.PARAMETER.getCode()).append(CRLF);
            sb.append(CONTENT_LENGTH).append(COLON + SP).append(names.length()).append(CRLF);
        }
    }

    @Override
    protected void addMessageBody(StringBuilder sb) {
        if (!this.parameterNames.isEmpty()) {
            String names = String.join(CRLF, this.parameterNames) + CRLF;
            sb.append(names);
        }
    }
}
