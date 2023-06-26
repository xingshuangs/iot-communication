package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpUdpClient;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtsp.service.IRtspDataStream;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
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

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public void setRtcpUdpClient(RtcpUdpClient rtcpUdpClient) {
        this.rtcpUdpClient = rtcpUdpClient;
    }

    public RtpUdpClient(IPayloadParser iPayloadParser) {
        this.iPayloadParser = iPayloadParser;
    }

    public RtpUdpClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return future;
    }

    @Override
    public void close() {
        this.terminal = true;
        super.close();
    }

    @Override
    public void triggerReceive() {
        this.future = CompletableFuture.runAsync(this::waitForReceiveData);
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
                    log.error(e.getMessage());
                }
            }
        }
        log.debug("[RTSP + UDP] RTP 关闭异步接收数据线程，远程的IP[/{}:{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
    }
}
