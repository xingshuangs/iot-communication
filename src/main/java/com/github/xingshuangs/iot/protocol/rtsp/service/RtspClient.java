package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Rtsp客户端
 *
 * @author xingshuang
 */
@Slf4j
public class RtspClient extends RtspNetwork {

    private boolean alive;

    public RtspClient(URI uri) {
        super(uri);
    }

    public RtspClient(URI uri, ERtspTransportProtocol transportProtocol) {
        super(uri, transportProtocol);
    }

    public RtspClient(URI uri, DigestAuthenticator authenticator) {
        super(uri, authenticator);
    }

    public RtspClient(URI uri, DigestAuthenticator authenticator, ERtspTransportProtocol transportProtocol) {
        super(uri, authenticator, transportProtocol);
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
            if (!this.methods.contains(ERtspMethod.GET_PARAMETER)) {
                return;
            }
            long lastTime = System.currentTimeMillis();
            while (this.alive) {
                TimeUnit.MILLISECONDS.sleep(200);
                if (System.currentTimeMillis() - lastTime > this.sessionInfo.getTimeout() / 2) {
                    lastTime = System.currentTimeMillis();
                    this.getParameter();
                }

            }
        } catch (Exception e) {
            throw new RtspCommException(e);
        } finally {
            if (this.alive) {
                this.disconnect();
            }
        }
    }

    /**
     * 断开
     */
    public void disconnect() {
        this.teardown();
        this.alive = false;
        this.close();
    }
}
