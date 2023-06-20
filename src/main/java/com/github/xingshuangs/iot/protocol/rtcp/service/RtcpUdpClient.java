package com.github.xingshuangs.iot.protocol.rtcp.service;


import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.model.*;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtsp.service.IRtspDataStream;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * RTCP协议的UDP客户端
 *
 * @author xingshuang
 */
@Slf4j
public class RtcpUdpClient extends UdpClientBasic implements IRtspDataStream {

    /**
     * 是否终止线程
     */
    private boolean terminal = false;

    /**
     * RTP和RTCP的数据统计
     */
    private final RtcpDataStatistics statistics = new RtcpDataStatistics();

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<byte[]> commCallback;

    /**
     * 异步执行对象
     */
    private CompletableFuture<Void> future;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }


    public RtcpUdpClient() {

    }

    public RtcpUdpClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return future;
    }

    @Override
    public void close() {
        if (!this.terminal) {
            // 发送byte
            byte[] receiverAndByteContent = this.statistics.createReceiverAndByteContent();
            this.sendData(receiverAndByteContent);
            this.terminal = true;
        }
        super.close();
    }

    /**
     * 触发接收
     */
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

    private void waitForReceiveData() {
        log.debug("[RTSP + UDP] RTCP 开启异步接收数据线程，远程的IP[{}]，端口号[{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.read();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(data);
                this.statistics.processRtcpPackage(basePackages);
            } catch (Exception e) {
                if (!this.terminal) {
                    log.error(e.getMessage());
                }
            }
        }
        log.debug("[RTSP + UDP] RTCP 关闭异步接收数据线程，远程的IP[{}]，端口号[{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
    }

    /**
     * 处理RTP的数据包
     *
     * @param rtpPackage RTP数据包
     */
    public void processRtpPackage(RtpPackage rtpPackage) {
        this.statistics.processRtpPackage(rtpPackage, this::sendData);
    }
}
