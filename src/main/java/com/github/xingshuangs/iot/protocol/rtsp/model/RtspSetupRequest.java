package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Setup请求
 *
 * @author xingshuang
 */
public class RtspSetupRequest extends RtspMessageRequest {

    private RtspTransport transport;

    public RtspSetupRequest(URI uri, RtspTransport transport) {
        this(uri, transport, null);
    }

    public RtspSetupRequest(URI uri, RtspTransport transport, AbstractAuthenticator authenticator) {
        super(ERtspMethod.SETUP, uri, authenticator);
        this.transport = transport;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (this.transport != null && !this.transport.equals("")) {
            sb.append(TRANSPORT).append(COLON + SP).append(this.transport).append(CRLF);
        }
    }
}
