/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        this(uri, null, transportProtocol);
    }

    public RtspClient(URI uri, DigestAuthenticator authenticator) {
        this(uri, authenticator, ERtspTransportProtocol.TCP);
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
        log.info("Open RTSP connection, address [{}], communication mode [{}]", this.uri, this.transportProtocol);
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
                        // 触发session心跳，发送参数获取信号
                        log.debug("[{}] triggers the session heartbeat and sends parameters to obtain signal", this.uri);
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
