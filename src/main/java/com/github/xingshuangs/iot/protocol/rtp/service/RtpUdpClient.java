package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.net.client.UdpClientBasic;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 简单UDP示例
 *
 * @author xingshuang
 */
@Slf4j
public class RtpUdpClient extends UdpClientBasic {

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
     * 帧处理回调事件
     */
    private Consumer<RawFrame> frameHandle;

    /**
     * 负载解析器
     */
    private IPayloadParser iPayloadParser;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public void setFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    public RtpUdpClient(IPayloadParser iPayloadParser) {
        this.iPayloadParser = iPayloadParser;
    }

    public RtpUdpClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void close() {
        this.terminal = true;
        super.close();
    }

    private void waitForReceiveData() {
        log.debug("RTP开启接收数据线程，远程的IP:{}，端口号：{}", this.serverAddress.getHostName(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.getReceiveData();
//                log.debug("数据长度：{}", data.length);
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                RtpPackage rtp = RtpPackage.fromBytes(data);
                this.iPayloadParser.processPackage(rtp, this::processFrame);
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
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = this.read(buffer);
        if (length < BUFFER_SIZE) {
            byte[] data = new byte[length];
            System.arraycopy(buffer, 0, data, 0, length);
            return data;
        } else {
            return buffer;
        }
    }

    /**
     * 处理帧数据
     *
     * @param frame 帧
     */
    private void processFrame(RawFrame frame) {
        if (this.frameHandle != null) {
            this.frameHandle.accept(frame);
        }
    }

    public void triggerReceive() {
        CompletableFuture.runAsync(this::waitForReceiveData);
    }
}
