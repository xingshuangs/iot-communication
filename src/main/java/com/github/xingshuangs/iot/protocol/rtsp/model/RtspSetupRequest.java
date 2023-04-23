package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspTransport;
import lombok.Getter;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Setup请求
 *
 * @author xingshuang
 */
@Getter
public final class RtspSetupRequest extends RtspMessageRequest {

    /**
     * 传输通道
     */
    private final RtspTransport transport;

    public RtspSetupRequest(URI uri, RtspTransport transport) {
        this(uri, transport, null);
    }

    public RtspSetupRequest(URI uri, RtspTransport transport, AbstractAuthenticator authenticator) {
        super(ERtspMethod.SETUP, uri, authenticator);
        this.transport = transport;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (this.transport != null) {
            sb.append(TRANSPORT).append(COLON + SP).append(this.transport).append(CRLF);
        }
    }
}
