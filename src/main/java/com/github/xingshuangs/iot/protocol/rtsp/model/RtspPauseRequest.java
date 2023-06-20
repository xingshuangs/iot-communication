package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;

/**
 * Play请求
 *
 * @author xingshuang
 */
@Getter
public final class RtspPauseRequest extends RtspMessageRequest {

    public RtspPauseRequest(URI uri, String session) {
        this(uri, session, null);
    }

    public RtspPauseRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        super(ERtspMethod.PAUSE, uri, session, authenticator);
    }
}
