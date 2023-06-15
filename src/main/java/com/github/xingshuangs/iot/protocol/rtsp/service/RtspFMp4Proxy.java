package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4Header;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4SampleData;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4TrackInfo;
import com.github.xingshuangs.iot.protocol.mp4.service.Mp4Generator;
import com.github.xingshuangs.iot.protocol.rtp.enums.EFrameType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspTrackInfo;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author xingshuang
 */
@Slf4j
public class RtspFMp4Proxy {
    /**
     * RTSP客户端
     */
    private final RtspClient client;

    /**
     * 轨道信息
     */
    private RtspTrackInfo trackInfo;

    /**
     * 接收帧数据的序列号
     */
    private long sequenceNumber = 0;

    /**
     * 数据缓存
     */
    private final List<IObjectByteArray> buffers = new LinkedList<>();

    /**
     * FMp4数据事件
     */
    private Consumer<byte[]> fmp4DataHandle;

    /**
     * 是否终止
     */
    private boolean terminal = false;

    /**
     * MP4的头
     */
    private Mp4Header mp4Header;

//    private final CompletableFuture<Void> future;

    public Mp4Header getMp4Header() {
        return mp4Header;
    }

    public void onFmp4DataHandle(Consumer<byte[]> fmp4DataHandle) {
        this.fmp4DataHandle = fmp4DataHandle;
    }

    public RtspFMp4Proxy(URI uri) {
        this(uri, null, ERtspTransportProtocol.UDP);
    }

    public RtspFMp4Proxy(URI uri, DigestAuthenticator authenticator) {
        this(uri, authenticator, ERtspTransportProtocol.UDP);
    }

    public RtspFMp4Proxy(URI uri, ERtspTransportProtocol transportProtocol) {
        this(uri, null, transportProtocol);
    }

    public RtspFMp4Proxy(URI uri, DigestAuthenticator authenticator, ERtspTransportProtocol transportProtocol) {
        this.client = new RtspClient(uri, authenticator, transportProtocol);
        this.client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            this.initHeaderHandle();
            this.frameHandle(f);
        });
//        this.future = CompletableFuture.runAsync(this::executeHandle);
    }

    private void initHeaderHandle() {
        if (this.trackInfo == null) {
            this.trackInfo = this.client.getTrackInfo();
            Mp4TrackInfo mp4TrackInfo = this.toMp4TrackInfo(0);
            this.mp4Header = Mp4Generator.createMp4Header(mp4TrackInfo);

            this.addFMp4Data(mp4Header);
        }
    }

    private void frameHandle(H264VideoFrame frame) {
        if (frame.getFrameType() == EFrameType.AUDIO) {
            return;
        }
        if (frame.getNaluType() == EH264NaluType.PPS
                || frame.getNaluType() == EH264NaluType.SPS
                || frame.getNaluType() == EH264NaluType.SEI) {
            return;
        }
        Mp4TrackInfo mp4TrackInfo = this.toMp4TrackInfo(frame.getTimestamp());
        Mp4SampleData sampleData = new Mp4SampleData();
        sampleData.setData(frame.getFrameSegment());
        sampleData.setSize(frame.getFrameSegment().length);
        if (frame.getNaluType() == EH264NaluType.IDR_SLICE) {
            sampleData.getFlags().setDependedOn(2);
            sampleData.getFlags().setIsNonSync(0);
        } else {
            sampleData.getFlags().setDependedOn(1);
            sampleData.getFlags().setIsNonSync(1);
        }
        mp4TrackInfo.setSampleData(Collections.singletonList(sampleData));
        this.sequenceNumber++;

        this.addFMp4Data(Mp4Generator.createFMp4Body(mp4TrackInfo));
    }

    private Mp4TrackInfo toMp4TrackInfo(long timestamp) {
        Mp4TrackInfo mp4TrackInfo = new Mp4TrackInfo();
        mp4TrackInfo.setSequenceNumber(this.sequenceNumber);
        mp4TrackInfo.setId(this.trackInfo.getId());
        mp4TrackInfo.setType(this.trackInfo.getType());
        mp4TrackInfo.setCodec(this.trackInfo.getCodec());
        mp4TrackInfo.setBaseMediaDecodeTime(timestamp);
        mp4TrackInfo.setTimescale(this.trackInfo.getTimescale());
        mp4TrackInfo.setDuration(this.trackInfo.getDuration());
        mp4TrackInfo.setWidth(this.trackInfo.getWidth());
        mp4TrackInfo.setHeight(this.trackInfo.getHeight());
        mp4TrackInfo.setSps(this.trackInfo.getSps());
        mp4TrackInfo.setPps(this.trackInfo.getPps());
        return mp4TrackInfo;
    }

    private void addFMp4Data(IObjectByteArray iObjectByteArray) {
        if (this.fmp4DataHandle != null) {
            this.fmp4DataHandle.accept(iObjectByteArray.toByteArray());
        }
//        this.buffers.add(iObjectByteArray);
//        synchronized (this.buffers) {
//            this.buffers.notifyAll();
//        }
    }

    private void executeHandle() {
        log.info("开启代理服务端异步发送FMp4字节数据");
        while (!this.terminal) {
            while (this.buffers.isEmpty() && !this.terminal) {
                synchronized (this.buffers) {
                    try {
                        this.buffers.wait();
                    } catch (InterruptedException e) {
                        log.debug(e.getMessage(), e);
                    }
                }
            }
            int size = this.buffers.size();
            List<IObjectByteArray> list = this.buffers.subList(0, size);
            int sum = list.stream().mapToInt(IObjectByteArray::byteArrayLength).sum();
            ByteWriteBuff buff = new ByteWriteBuff(sum);
            list.forEach(x -> buff.putBytes(x.toByteArray()));
            list.clear();
            log.debug("当前缓存数量：{}", this.buffers.size());

            if (this.fmp4DataHandle != null) {
                this.fmp4DataHandle.accept(buff.getData());
            }
        }
        log.info("关闭代理服务端异步发送FMp4字节数据");
    }

    /**
     * 开始
     */
    public void start() {
        this.client.start();
    }

    /**
     * 结束
     */
    public void stop() {
        this.terminal = true;
//        synchronized (this.buffers) {
//            this.buffers.notifyAll();
//        }
        this.client.stop();
    }
}
