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

    private String transport = "";

    public RtspSetupRequest(URI uri, String transport) {
        this(uri, transport, null);
    }

    public RtspSetupRequest(URI uri, String transport, AbstractAuthenticator authenticator) {
        super(ERtspMethod.SETUP, uri, authenticator);
        this.transport = transport;
    }

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toObjectString());

        if (this.transport != null && !this.transport.equals("")) {
            sb.append(TRANSPORT).append(COLON).append(this.transport).append(CRLF);
        }
        return sb.toString();
    }
}
