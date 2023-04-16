package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspContentType;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_LENGTH;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_TYPE;

/**
 * GetParameter请求
 *
 * @author xingshuang
 */
@Getter
public class RtspGetParameterRequest extends RtspMessageRequest {

    /**
     * 有序set集合，参数名
     */
    private final Set<String> parameterNames = new LinkedHashSet<>();

    public RtspGetParameterRequest(URI uri, int session) {
        super(ERtspMethod.GET_PARAMETER, uri, session);
    }

    public RtspGetParameterRequest(URI uri, int session, AbstractAuthenticator authenticator) {
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
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toObjectString());

        if (!this.parameterNames.isEmpty()) {
            String names = String.join(CRLF, this.parameterNames);
            sb.append(CONTENT_TYPE).append(COLON).append(ERtspContentType.PARAMETER.getCode()).append(CRLF);
            sb.append(CONTENT_LENGTH).append(COLON).append(names.length()).append(CRLF);
            sb.append(CRLF);
            sb.append(names);
        }
        return sb.toString();
    }
}
