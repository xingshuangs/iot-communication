package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.protocol.rtsp.sdp.RtspSdp;
import com.github.xingshuangs.iot.protocol.rtsp.sdp.RtspSdpMedia;
import com.github.xingshuangs.iot.protocol.rtsp.sdp.RtspSdpSession;
import org.junit.Test;

import static org.junit.Assert.*;


public class RtspMessageResponseTest {

    @Test
    public void rtspOptionResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Public: DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE\r\n" +
                "\r\n";
        RtspOptionResponse response = RtspOptionResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(5, response.getPublicMethods().size());
    }

    @Test
    public void rtspDescribeResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Content-Type: application/sdp\r\n" +
                "Content-Base: rtsp://10.3.8.202:554/\r\n" +
                "Content-Length: 551\r\n" +
                "\r\n";
        String body = "v=0\r\n" +
                "o=- 1517245007527432 1517245007527432 IN IP4 10.3.8.202\r\n" +
                "s=Media Presentation\r\n" +
                "e=NONE\r\n" +
                "b=AS:5050\r\n" +
                "t=0 0\r\n" +
                "a=control:rtsp://10.3.8.202:554/\r\n" +
                "m=video 0 RTP/AVP 96\r\n" +
                "c=IN IP4 0.0.0.0\r\n" +
                "b=AS:5000\r\n" +
                "a=recvonly\r\n" +
                "a=x-dimensions:2048,1536\r\n" +
                "a=control:rtsp://10.3.8.202:554/trackID=1\r\n" +
                "a=rtpmap:96 H264/90000\r\n" +
                "a=fmtp:96 profile-level-id=420029; packetization-mode=1; sprop-parameter-sets=Z00AH5Y1QKALdNwEBAQI,aO48gA==\r\n" +
                "a=Media_header:MEDIAINFO=494D4B48010200000400000100000000000000000000000000000000000000000000000000000000;\r\n" +
                "a=appversion:1.0\r\n";
        RtspDescribeResponse response = RtspDescribeResponse.fromHeaderString(src);
        response.addBodyFromString(body);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("application/sdp", response.getContentType().getCode());
        assertEquals("rtsp://10.3.8.202:554/", response.getContentBase());
        assertEquals(551, response.getContentLength().intValue());

        RtspSdpSession session = response.getSdp().getSession();
        assertEquals(0, session.getVersion().intValue());
        assertEquals("1517245007527432", session.getOrigin().getSessionId());
        assertEquals("1517245007527432", session.getOrigin().getSessionVersion());
        assertEquals("IN", session.getOrigin().getNetworkType());
        assertEquals("IP4", session.getOrigin().getAddressType());
        assertEquals("10.3.8.202", session.getOrigin().getUnicastAddress());
        assertEquals("Media Presentation", session.getSessionName());
        assertEquals("NONE", session.getEmail());
        assertEquals("AS", session.getBandwidth().getType());
        assertEquals(5050, session.getBandwidth().getValue().intValue());
        assertEquals(0, session.getTime().getStartTime().intValue());
        assertEquals(0, session.getTime().getEndTime().intValue());
        assertEquals(1, session.getAttributes().size());

        RtspSdp sdp = response.getSdp();
        assertEquals(1, sdp.getMedias().size());
        RtspSdpMedia media = sdp.getMedias().get(0);
        assertEquals("video", media.getMediaDesc().getType());
        assertEquals(0, media.getMediaDesc().getPort().intValue());
        assertEquals("RTP/AVP", media.getMediaDesc().getProtocol());
        assertEquals(96, media.getMediaDesc().getPayloadFormatNumber().intValue());
        assertEquals("IN", media.getConnection().getNetworkType());
        assertEquals("IP4", media.getConnection().getAddressType());
        assertEquals("0.0.0.0", media.getConnection().getConnectionAddress());
        assertEquals("AS", media.getBandwidth().getType());
        assertEquals(5000, media.getBandwidth().getValue().intValue());
        assertEquals(7, media.getAttributes().size());

        assertEquals(1536, media.getAttributeDimension().getHeight().intValue());
        assertEquals(2048, media.getAttributeDimension().getWidth().intValue());
        assertEquals(1, media.getAttributeControl().getTrackID().intValue());
        assertEquals("rtsp://10.3.8.202:554/trackID=1", media.getAttributeControl().getUri());
        assertEquals(96, media.getAttributeRtpMap().getPayloadNumber().intValue());
        assertEquals(90000, media.getAttributeRtpMap().getClockFrequency().intValue());
        assertEquals("H264", media.getAttributeRtpMap().getPayloadFormat());
        assertEquals(96, media.getAttributeFmtp().getNumber().intValue());
        assertEquals(420029, media.getAttributeFmtp().getProfileLevelId().intValue());
        assertEquals(1, media.getAttributeFmtp().getPacketizationMode().intValue());
        assertEquals("Z00AH5Y1QKALdNwEBAQI,aO48gA==", media.getAttributeFmtp().getSpropParameterSets());
        byte[] sps = new byte[]{(byte) 0x67, (byte) 0x4D, (byte) 0x00, (byte) 0x1F, (byte) 0x96, (byte) 0x35, (byte) 0x40, (byte) 0xA0, (byte) 0x0B, (byte) 0x74, (byte) 0xDC, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08};
        assertArrayEquals(sps, media.getAttributeFmtp().getSps());
        byte[] pps = new byte[]{(byte) 0x68, (byte) 0xEE, (byte) 0x3C, (byte) 0x80};
        assertArrayEquals(pps, media.getAttributeFmtp().getPps());
        assertEquals("494D4B48010200000400000100000000000000000000000000000000000000000000000000000000", media.getAttributeHeader().getMediaInfo());
    }

    @Test
    public void rtspSetupResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 814632351;timeout=60\r\n" +
                "Transport: RTP/AVP;unicast;client_port=8000-8001;server_port=9000-9001;ssrc=1234ABCD\r\n" +
                "\r\n";

        RtspSetupResponse response = RtspSetupResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("814632351", response.getSession());
        assertEquals("814632351", response.getSessionInfo().getSessionId());
        assertEquals(60_000, response.getSessionInfo().getTimeout());
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

        RtspPlayResponse response = RtspPlayResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("123456", response.getSession());
        assertEquals(1, response.getRtpInfo().size());
        assertEquals("rtsp://10.3.8.202:554/trackID=1", response.getRtpInfo().get(0).getUrl());
        assertEquals(65373, response.getRtpInfo().get(0).getSeq().longValue());
        assertEquals(3566398668L, response.getRtpInfo().get(0).getRtpTime().longValue());

        src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "RTP-Info: url=rtsp://192.17.1.63:554/trackID=1;seq=3658;rtptime=1710363406,url=rtsp://192.17.1.63:554/trackID=2;seq=6598;rtptime=4065225152\r\n" +
                "\r\n";

        response = RtspPlayResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("123456", response.getSession());
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

        RtspPauseResponse response = RtspPauseResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("123456", response.getSession());
    }

    @Test
    public void rtspTeardownResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456\r\n" +
                "\r\n";

        RtspTeardownResponse response = RtspTeardownResponse.fromHeaderString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals("123456", response.getSession());
    }

    @Test
    public void rtspGetParameterResponse() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "\r\n";
        String body = "packets_received: 10\r\n" +
                "jitter: 0.3838\r\n";
        RtspGetParameterResponse response = RtspGetParameterResponse.fromHeaderString(src);
        response.addBodyFromString(body);
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
                "\r\n";
        String body = "packets_received\r\n";
        RtspSetParameterResponse response = RtspSetParameterResponse.fromHeaderString(src);
        response.addBodyFromString(body);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.getParameterNames().size());
    }
}