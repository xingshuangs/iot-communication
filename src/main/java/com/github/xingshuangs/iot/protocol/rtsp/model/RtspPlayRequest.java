package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.RANGE;

/**
 * Play请求
 *
 * @author xingshuang
 */
@Getter
public class RtspPlayRequest extends RtspMessageRequest {

    private String range = "";

    public RtspPlayRequest(URI uri, int session) {
        this(uri, session, null, null);
    }

    public RtspPlayRequest(URI uri, int session, String range) {
        this(uri, session, range, null);
    }

    public RtspPlayRequest(URI uri, int session, String range, AbstractAuthenticator authenticator) {
        super(ERtspMethod.PLAY, uri, session, authenticator);
        this.range = range;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (this.range != null && !this.range.equals("")) {
            sb.append(RANGE).append(COLON + SP).append(this.range).append(CRLF);
        }
    }
}
