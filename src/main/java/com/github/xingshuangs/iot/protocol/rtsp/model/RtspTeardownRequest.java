package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;

/**
 * Teardown请求
 *
 * @author xingshuang
 */
@Getter
public final class RtspTeardownRequest extends RtspMessageRequest {

    public RtspTeardownRequest(URI uri, String session) {
        this(uri, session, null);
    }

    public RtspTeardownRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        super(ERtspMethod.TEARDOWN, uri, session, authenticator);
    }
}
