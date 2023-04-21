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
    /**
     * 时间范围
     */
    private final RtspRange range;

    public RtspPlayRequest(URI uri, String session) {
        this(uri, session, null, null);
    }

    public RtspPlayRequest(URI uri, String session, RtspRange range) {
        this(uri, session, range, null);
    }

    public RtspPlayRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        this(uri, session, null, authenticator);
    }

    public RtspPlayRequest(URI uri, String session, RtspRange range, AbstractAuthenticator authenticator) {
        super(ERtspMethod.PLAY, uri, session, authenticator);
        this.range = range;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (this.range != null) {
            sb.append(RANGE).append(COLON + SP).append(this.range).append(CRLF);
        }
    }
}
