package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpBasePackage;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpPackageBuilder;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpSenderReport;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpDataStatistics;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import com.github.xingshuangs.iot.protocol.rtp.service.IPayloadParser;
import com.github.xingshuangs.iot.protocol.rtsp.model.interleaved.RtspInterleaved;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 简单TCP示例
 *
 * @author xingshuang
 */
@Slf4j
public class RtspInterleavedClient implements IRtspDataStream {

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
     * 上一次接收到RTP的时间
     */
    private long lastTimeReceiveRtp = 0;

    /**
     * 负载解析器
     */
    private IPayloadParser iPayloadParser;

    /**
     * RTP和RTCP的数据统计
     */
    private final RtcpDataStatistics statistics = new RtcpDataStatistics();

    private int rtpVideoChannelNumber = 0;

    private int rtcpVideoChannelNumber = 1;

    private RtspNetwork rtspClient;

    /**
     * 异步执行对象
     */
    private CompletableFuture<Void> future;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public void setFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    public int getRtpVideoChannelNumber() {
        return rtpVideoChannelNumber;
    }

    public int getRtcpVideoChannelNumber() {
        return rtcpVideoChannelNumber;
    }

    public void setRtpVideoChannelNumber(int rtpVideoChannelNumber) {
        this.rtpVideoChannelNumber = rtpVideoChannelNumber;
    }

    public void setRtcpVideoChannelNumber(int rtcpVideoChannelNumber) {
        this.rtcpVideoChannelNumber = rtcpVideoChannelNumber;
    }

    public RtspInterleavedClient(IPayloadParser iPayloadParser, RtspNetwork rtspClient) {
        this.iPayloadParser = iPayloadParser;
        this.rtspClient = rtspClient;
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return this.future;
    }

    @Override
    public void close() {
        this.sendByte();
        this.terminal = true;
    }

    private void waitForReceiveData() {
        log.info("[RTSP+TCP] Interleaved 开启异步接收数据线程");
        while (!this.terminal) {
            try {
                byte[] data = this.rtspClient.readFromServer();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                RtspInterleaved interleaved = RtspInterleaved.fromBytes(data);

                if (interleaved.getChannelId() == this.rtpVideoChannelNumber) {
                    this.rtpVideoHandle(interleaved);
                } else if (interleaved.getChannelId() == this.rtcpVideoChannelNumber) {
                    this.rtcpVideoHandle(interleaved);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 处理视频的RTCP
     *
     * @param interleaved 数据包
     */
    private void rtcpVideoHandle(RtspInterleaved interleaved) {
        List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(interleaved.getPayload());
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
    }

    /**
     * 处理视频RTP
     *
     * @param interleaved 数据包
     */
    private void rtpVideoHandle(RtspInterleaved interleaved) {
        RtpPackage rtp = RtpPackage.fromBytes(interleaved.getPayload());
//        log.debug("数据长度[{}], 时间戳[{}], 序列号[{}]", rtp.byteArrayLength(), rtp.getHeader().getTimestamp(), rtp.getHeader().getSequenceNumber());
        this.iPayloadParser.processPackage(rtp, this::processFrame);
        this.statistics.processRtpPackage(rtp);

        // 第一次接收RTP数据包
        if (this.lastTimeReceiveRtp == 0) {
            this.lastTimeReceiveRtp = System.currentTimeMillis();
        }
        // 时间间隔超过5s的发一次RR数据
        if (System.currentTimeMillis() - this.lastTimeReceiveRtp > 5_000) {
            this.sendRR();
            this.lastTimeReceiveRtp = System.currentTimeMillis();
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

    @Override
    public void triggerReceive() {
        this.future = CompletableFuture.runAsync(this::waitForReceiveData);
    }

    /**
     * 发送BYTE
     */
    private void sendByte() {
        byte[] receiverAndByteContent = this.statistics.createReceiverAndByteContent();
        this.rtspClient.write(receiverAndByteContent);
    }

    /**
     * 发送RR
     */
    private void sendRR() {
        byte[] receiverAndSdesContent = this.statistics.createReceiverAndSdesContent();
        this.rtspClient.write(receiverAndSdesContent);
    }
}
