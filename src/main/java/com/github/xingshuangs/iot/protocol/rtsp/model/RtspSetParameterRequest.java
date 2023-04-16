package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;

/**
 * GetParameter请求
 *
 * @author xingshuang
 */
@Getter
public class RtspSetParameterRequest extends RtspMessageRequest {

    private final Map<String, String> parameterMap = new LinkedHashMap<>();

    public RtspSetParameterRequest(URI uri, int session) {
        super(ERtspMethod.SET_PARAMETER, uri, session);
    }

    public RtspSetParameterRequest(URI uri, int session, AbstractAuthenticator authenticator) {
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
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toObjectString());

        if (!this.parameterMap.isEmpty()) {
            sb.append(CRLF);
            this.parameterMap.forEach((key, value) -> sb.append(key).append(COLON).append(value).append(CRLF));
        }
        return sb.toString();
    }
}
