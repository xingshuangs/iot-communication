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
     * 上一次接收到RTP的时间
     */
    private long lastTimeReceiveRtp = 0;

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
            this.sendByte();
            this.terminal = true;
        }
        super.close();
    }

    private void waitForReceiveData() {
        log.info("[RTSP+UDP] RTCP 开启异步接收数据线程，远程的IP[{}]，端口号[{}]",
                this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.getReceiveData();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(data);
                basePackages.forEach(x -> log.debug("RTCP接收[{}]数据，{}", x.getHeader().getPackageType(), x));
                for (RtcpBasePackage basePackage : basePackages) {
                    switch (basePackage.getHeader().getPackageType()) {
                        case SR:
                            this.statistics.processRtcpPackage((RtcpSenderReport) basePackage);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取接收的数据
     *
     * @return 字节数组
     */
    private byte[] getReceiveData() {
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int length = this.read(buffer);
        if (length < bufferSize) {
            byte[] data = new byte[length];
            System.arraycopy(buffer, 0, data, 0, length);
            return data;
        } else {
            return buffer;
        }
    }

    /**
     * 触发接收
     */
    @Override
    public void triggerReceive() {
        this.future = CompletableFuture.runAsync(this::waitForReceiveData);
    }

    /**
     * 处理RTP的数据包
     *
     * @param rtpPackage RTP数据包
     */
    public void processRtpPackage(RtpPackage rtpPackage) {
        this.statistics.processRtpPackage(rtpPackage);
//        log.debug("统计数据：{}", this.statistics);
        // 第一次接收RTP数据包
        if (this.lastTimeReceiveRtp == 0) {
            this.lastTimeReceiveRtp = System.currentTimeMillis();
        }
        // 时间间隔超过5s的发一次RR数据
        if (System.currentTimeMillis() - this.lastTimeReceiveRtp > 5_000) {
            byte[] receiverAndSdesContent = this.statistics.createReceiverAndSdesContent();
            this.write(receiverAndSdesContent);
            this.lastTimeReceiveRtp = System.currentTimeMillis();
        }
    }

    /**
     * 发送BYTE
     */
    private void sendByte() {
        byte[] receiverAndByteContent = this.statistics.createReceiverAndByteContent();
        this.write(receiverAndByteContent);
    }
}
