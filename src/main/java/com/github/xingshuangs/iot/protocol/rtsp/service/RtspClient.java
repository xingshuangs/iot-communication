package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Rtsp客户端
 *
 * @author xingshuang
 */
public class RtspClient extends RtspNetwork {

    private boolean alive;

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
        this.alive = true;
    }

    public void receive() {
        try {
            while (this.alive) {
                this.getParameter();
                TimeUnit.SECONDS.sleep(this.sessionInfo.getTimeout() / 2);
            }
        } catch (Exception e) {
            throw new RtspCommException(e);
        } finally {
            if(this.alive) {
                this.teardown();
                this.alive = false;
            }
        }
    }

    /**
     * 断开
     */
    public void disconnect() {
        this.teardown();
        this.alive = false;
    }
}
