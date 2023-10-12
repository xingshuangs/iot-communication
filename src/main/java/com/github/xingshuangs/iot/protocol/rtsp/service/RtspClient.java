package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Rtsp客户端
 *
 * @author xingshuang
 */
@Slf4j
public class RtspClient extends RtspNetwork {

    private boolean alive;

    /**
     * 线程池执行服务，单线程
     */
    private ExecutorService executorService;

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
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 启动
     *
     * @return 执行的future
     */
    public CompletableFuture<Void> start() {
        log.info("开启RTSP连接，地址[{}]，通信模式[{}]", this.uri, this.transportProtocol);
        this.connect();
        // 保证连接成功后在置true
        this.alive = true;

        return CompletableFuture.runAsync(() -> {
            try {
                if (!this.methods.contains(ERtspMethod.GET_PARAMETER)) {
                    this.socketClientJoinForFinished();
                    return;
                }
                long lastTime = System.currentTimeMillis();
                while (this.alive) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    // 所有线程都已经完成
                    if (this.socketClientIsAllDone()) {
                        break;
                    }
                    if (System.currentTimeMillis() - lastTime > (this.sessionInfo.getTimeout() - 1) / 2) {
                        lastTime = System.currentTimeMillis();
                        log.debug("[{}]触发session心跳，发送参数获取信号", this.uri);
                        this.getParameter();
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (this.alive) {
                    this.stop();
                }
            }
        }, this.executorService);
    }

    /**
     * 断开
     */
    public void stop() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.alive) {
            this.alive = false;
            this.teardown();
            this.close();
        }
    }
}
