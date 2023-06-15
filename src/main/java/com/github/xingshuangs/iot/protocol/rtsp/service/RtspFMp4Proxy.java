package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4SampleData;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4TrackInfo;
import com.github.xingshuangs.iot.protocol.mp4.service.Mp4Generator;
import com.github.xingshuangs.iot.protocol.rtp.enums.EFrameType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspTrackInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xingshuang
 */
public class RtspFMp4Proxy {
    private final RtspClient client;

    private RtspTrackInfo trackInfo;

    private long sequenceNumber = 0;

    private final List<IObjectByteArray> buffers = new ArrayList<>();

    public RtspFMp4Proxy() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        this.client = new RtspClient(uri, authenticator);
        this.client.setFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            this.initHandle(f);
            this.frameHandle(f);
        });

    }

    private void initHandle(H264VideoFrame frame) {
        if (this.trackInfo == null) {
            this.trackInfo = this.client.getTrackInfo();
            Mp4TrackInfo mp4TrackInfo = getMp4TrackInfo(frame);
            buffers.add(Mp4Generator.createMp4Header(mp4TrackInfo));
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
        Mp4TrackInfo mp4TrackInfo = getMp4TrackInfo(frame);
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
    }

    private Mp4TrackInfo getMp4TrackInfo(H264VideoFrame frame) {
        Mp4TrackInfo mp4TrackInfo = new Mp4TrackInfo();
        mp4TrackInfo.setSequenceNumber(this.sequenceNumber);
        mp4TrackInfo.setId(this.trackInfo.getId());
        mp4TrackInfo.setType(this.trackInfo.getType());
        mp4TrackInfo.setCodec(this.trackInfo.getCodec());
        mp4TrackInfo.setBaseMediaDecodeTime(frame.getTimestamp());
        mp4TrackInfo.setTimescale(this.trackInfo.getTimescale());
        mp4TrackInfo.setDuration(this.trackInfo.getDuration());
        mp4TrackInfo.setWidth(this.trackInfo.getWidth());
        mp4TrackInfo.setHeight(this.trackInfo.getHeight());
        mp4TrackInfo.setSps(this.trackInfo.getSps());
        mp4TrackInfo.setPps(this.trackInfo.getPps());
        return mp4TrackInfo;
    }

    public void start() {
        this.client.start();
    }

    public void stop() {
        this.client.stop();
    }
}
