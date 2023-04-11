package com.github.xingshuangs.iot.protocol.rtsp.model;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;


public class RtspOptionRequestTest {

    @Test
    public void toObjectString() {
        String expect = "OPTIONS * RTSP/1.0\r\n" +
                "CSeq:0\r\n" +
                "User-Agent:IOT-COMMUNICATION\r\n";
        URI uri = URI.create("*");
        RtspOptionRequest request = new RtspOptionRequest(uri);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

}