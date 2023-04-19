package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class RtspMessageResponseTest {

    @Test
    public void rtspOptionResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Public: DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE\r\n" +
                "\r\n";
        RtspOptionResponse response = RtspOptionResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(5, response.getPublicMethods().size());
    }

    @Test
    public void rtspSetupResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "Transport: RTP/AVP;unicast;client_port=8000-8001;server_port=9000-9001;ssrc=1234ABCD\r\n" +
                "\r\n";

        RtspSetupResponse response = RtspSetupResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
        String expect = "RTP/AVP;unicast;client_port=8000-8001;server_port=9000-9001;ssrc=1234ABCD";
        assertEquals(expect, response.getTransport().toString());
    }

    @Test
    public void rtspPlayResponse() {

        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "RTP-Info: url=rtsp://10.3.8.202:554/trackID=1;seq=65373;rtptime=3566398668\r\n" +
                "\r\n";

        RtspPlayResponse response = RtspPlayResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
        assertEquals(1, response.getRtpInfo().size());
        assertEquals("rtsp://10.3.8.202:554/trackID=1", response.getRtpInfo().get(0).getUrl());
        assertEquals(65373, response.getRtpInfo().get(0).getSeq().longValue());
        assertEquals(3566398668L, response.getRtpInfo().get(0).getRtpTime().longValue());

        src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "RTP-Info: url=rtsp://192.17.1.63:554/trackID=1;seq=3658;rtptime=1710363406,url=rtsp://192.17.1.63:554/trackID=2;seq=6598;rtptime=4065225152\r\n" +
                "\r\n";

        response = RtspPlayResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
        assertEquals(2, response.getRtpInfo().size());
        assertEquals("rtsp://192.17.1.63:554/trackID=1", response.getRtpInfo().get(0).getUrl());
        assertEquals(3658, response.getRtpInfo().get(0).getSeq().longValue());
        assertEquals(1710363406L, response.getRtpInfo().get(0).getRtpTime().longValue());
        assertEquals("rtsp://192.17.1.63:554/trackID=2", response.getRtpInfo().get(1).getUrl());
        assertEquals(6598, response.getRtpInfo().get(1).getSeq().longValue());
        assertEquals(4065225152L, response.getRtpInfo().get(1).getRtpTime().longValue());
    }

    @Test
    public void rtspPauseResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "\r\n";

        RtspPauseResponse response = RtspPauseResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
    }

    @Test
    public void rtspTeardownResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "\r\n";

        RtspTeardownResponse response = RtspTeardownResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
    }

    @Test
    public void rtspGetParameterResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "\r\n" +
                "packets_received: 10\r\n" +
                "jitter: 0.3838\r\n";
        RtspGetParameterResponse response = RtspGetParameterResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(2, response.getParameters().size());
    }

    @Test
    public void rtspSetParameterResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Content-Length: 18\r\n" +
                "Content-type: text/parameters\r\n" +
                "\r\n" +
                "packets_received\r\n";
        RtspSetParameterResponse response = RtspSetParameterResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.getParameterNames().size());
    }
}