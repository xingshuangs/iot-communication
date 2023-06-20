package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspClientPortTransport;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspInterleavedTransport;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspTransport;
import org.junit.Test;

import static org.junit.Assert.*;


public class RtspTransportTest {

    @Test
    public void extractFrom() {
        String src = "RTP/AVP;unicast;client_port=60802-60803;server_port=8216-8217;ssrc=4f92ef4b;mode=\"play\"";
        RtspClientPortTransport transport = (RtspClientPortTransport)RtspTransport.fromString(src);
        assertEquals("RTP/AVP", transport.getProtocol());
        assertEquals("unicast", transport.getCastMode());
        assertEquals(60802, transport.getRtpClientPort().intValue());
        assertEquals(60803, transport.getRtcpClientPort().intValue());
        assertEquals(8216, transport.getRtpServerPort().intValue());
        assertEquals(8217, transport.getRtcpServerPort().intValue());
        assertEquals("4f92ef4b", transport.getSsrc());
        assertEquals("play", transport.getMode());
        assertEquals(src, transport.toString());
    }

    @Test
    public void rtspInterleavedTransportTest() {
        String src = "RTP/AVP/TCP;unicast;interleaved=0-1;ssrc=1fc17e75;mode=\"play\"";
        RtspInterleavedTransport transport = (RtspInterleavedTransport)RtspTransport.fromString(src);
        assertEquals("RTP/AVP/TCP", transport.getProtocol());
        assertEquals("unicast", transport.getCastMode());
        assertEquals(0, transport.getInterleaved1().intValue());
        assertEquals(1, transport.getInterleaved2().intValue());
        assertEquals("1fc17e75", transport.getSsrc());
        assertEquals("play", transport.getMode());
        assertEquals(src, transport.toString());
    }

    @Test
    public void rtspClientPortTransportToString1() {
        String expect = "RTP/AVP;unicast;client_port=60802-60803";
        RtspClientPortTransport transport = new RtspClientPortTransport();
        transport.setProtocol("RTP/AVP");
        transport.setCastMode("unicast");
        transport.setRtpClientPort(60802);
        transport.setRtcpClientPort(60803);
        assertEquals(expect, transport.toString());
    }

    @Test
    public void rtspInterleavedTransportToString() {
        String expect = "RTP/AVP/TCP;unicast;interleaved=0-1";
        RtspInterleavedTransport transport = new RtspInterleavedTransport();
        transport.setProtocol("RTP/AVP/TCP");
        transport.setCastMode("unicast");
        transport.setInterleaved1(0);
        transport.setInterleaved2(1);
        assertEquals(expect, transport.toString());
    }
}