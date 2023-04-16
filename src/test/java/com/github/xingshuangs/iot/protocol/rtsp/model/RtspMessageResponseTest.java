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
        assertEquals("url=rtsp://10.3.8.202:554/trackID=1;seq=65373;rtptime=3566398668", response.getRtpInfo());
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