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

package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpUdpClient;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtsp.service.IRtspDataStream;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 简单UDP示例
 *
 * @author xingshuang
 */
@Slf4j
public class RtpUdpClient extends UdpClientBasic implements IRtspDataStream {

    /**
     * 是否终止线程
     */
    private boolean terminal = false;

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<byte[]> commCallback;

    /**
     * 负载解析器
     */
    private IPayloadParser iPayloadParser;

    /**
     * 异步执行对象
     */
    private CompletableFuture<Void> future;

    /**
     * rtcp的客户端
     */
    private RtcpUdpClient rtcpUdpClient;

    /**
     * 线程池执行服务，单线程
     */
    private final ExecutorService executorService;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public void setRtcpUdpClient(RtcpUdpClient rtcpUdpClient) {
        this.rtcpUdpClient = rtcpUdpClient;
    }

    public RtpUdpClient(IPayloadParser iPayloadParser) {
        this.iPayloadParser = iPayloadParser;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public RtpUdpClient(String ip, int port) {
        super(ip, port);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return future;
    }

    @Override
    public void close() {
        this.executorService.shutdown();
        this.terminal = true;
        super.close();
    }

    @Override
    public void triggerReceive() {
        this.future = CompletableFuture.runAsync(this::waitForReceiveData, this.executorService);
    }

    @Override
    public void sendData(byte[] data) {
        if (this.commCallback != null) {
            this.commCallback.accept(data);
        }
        this.write(data);
    }

    /**
     * 接收数据的线程
     */
    private void waitForReceiveData() {
        log.debug("[RTSP + UDP] RTP 开启异步接收数据线程，远程的IP[/{}:{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.read();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                RtpPackage rtp = RtpPackage.fromBytes(data);
                if (data.length > rtp.byteArrayLength()) {
                    log.warn("rtp数据还有未处理部分，未处理字节个数[{}]", data.length - rtp.byteArrayLength());
                }
                if (this.rtcpUdpClient != null) {
                    this.rtcpUdpClient.processRtpPackage(rtp);
                }
                this.iPayloadParser.processPackage(rtp);
            } catch (SocketRuntimeException e) {
                // SocketRuntimeException就是IO异常，网络断开了，结束线程
                log.error(e.getMessage());
                this.terminal = true;
                break;
            } catch (Exception e) {
                if (!this.terminal) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        log.debug("[RTSP + UDP] RTP 关闭异步接收数据线程，远程的IP[/{}:{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
    }
}
