package com.github.xingshuangs.iot.protocol.rtcp.service;


import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.model.*;
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
public class RtcpUdpClient extends UdpClientBasic {

    /**
     * 是否终止线程
     */
    private boolean terminal = false;

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<byte[]> commCallback;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public RtcpUdpClient() {
    }

    public RtcpUdpClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void close() {
        this.terminal = true;
        super.close();
    }

    private void waitForReceiveData() {
        log.debug("RTCP开启接收数据线程，远程的IP:{}，端口号：{}", this.serverAddress.getHostName(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.getReceiveData();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(data);
                basePackages.forEach(x -> log.debug("RTCP接收数据，{}", x));
                for (RtcpBasePackage basePackage : basePackages) {
                    switch (basePackage.getHeader().getPackageType()){
                        case SR:
                            break;
                        case BYE:
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
    public void triggerReceive() {
        CompletableFuture.runAsync(this::waitForReceiveData);
    }

    /**
     * 发送接收者和描述信息
     *
     * @param receiverReport 接收者
     * @param sdesReport     描述
     */
    public void sendReceiverAndSdes(RtcpReceiverReport receiverReport, RtcpSdesReport sdesReport) {
        byte[] res = new byte[receiverReport.byteArrayLength() + sdesReport.byteArrayLength()];
        System.arraycopy(receiverReport.toByteArray(), 0, res, 0, receiverReport.byteArrayLength());
        System.arraycopy(sdesReport.toByteArray(), 0, res, receiverReport.byteArrayLength(), sdesReport.byteArrayLength());
        this.write(res);
    }
}
