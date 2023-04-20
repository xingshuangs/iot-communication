package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;

import java.net.URI;

/**
 * Rtsp客户端
 *
 * @author xingshuang
 */
public class RtspClient extends RtspNetwork {

    public RtspClient(URI uri) {
        super(uri);
    }

    public RtspClient(URI uri, DigestAuthenticator authenticator) {
        super(uri, authenticator);
    }

    /**
     * 连接
     */
    public void connect() {
        this.option();

        this.describe();

        this.setup();

        this.play();
    }

    /**
     * 断开
     */
    public void disconnect() {
        this.teardown();
    }
}
