package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRtpInfo;
import org.junit.Test;

import static org.junit.Assert.*;


public class RtspRtpInfoTest {

    @Test
    public void fromString() {
        String src = "url=rtsp://192.168.3.250:554/h264/ch1/main/av_stream/trackID=1;seq=36755;rtptime=29143980";
        RtspRtpInfo rtpInfo = RtspRtpInfo.fromString(src);
        assertEquals("rtsp://192.168.3.250:554/h264/ch1/main/av_stream/trackID=1", rtpInfo.getUrl());
        assertEquals(36755, rtpInfo.getSeq().intValue());
        assertEquals(29143980, rtpInfo.getRtpTime().intValue());
    }
}