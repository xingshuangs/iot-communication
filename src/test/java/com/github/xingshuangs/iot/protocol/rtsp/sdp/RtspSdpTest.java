package com.github.xingshuangs.iot.protocol.rtsp.sdp;

import org.junit.Test;

import static org.junit.Assert.*;


public class RtspSdpTest {

    @Test
    public void fromString() {
        String src = "v=0\r\n" +
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

        RtspSdp sdp = RtspSdp.fromString(src);
        RtspSdpSession session = sdp.getSession();
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
}