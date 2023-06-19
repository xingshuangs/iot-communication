package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpUdpClient;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
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

    public static final Integer BUFFER_SIZE = 4096;

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
        log.debug("[RTSP + UDP] RTP 开启异步接收数据线程，远程的IP[{}]，端口号[{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.read();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                RtpPackage rtp = RtpPackage.fromBytes(data);
//                log.debug("数据长度[{}], 时间戳[{}], 序列号[{}]", rtp.byteArrayLength(), rtp.getHeader().getTimestamp(), rtp.getHeader().getSequenceNumber());
                if (this.rtcpUdpClient != null) {
                    this.rtcpUdpClient.processRtpPackage(rtp);
                }
                this.iPayloadParser.processPackage(rtp);
            } catch (Exception e) {
                if (!this.terminal) {
                    log.error(e.getMessage());
                }
            }
        }
        log.debug("[RTSP + UDP] RTP 关闭异步接收数据线程，远程的IP[{}]，端口号[{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
    }
}
