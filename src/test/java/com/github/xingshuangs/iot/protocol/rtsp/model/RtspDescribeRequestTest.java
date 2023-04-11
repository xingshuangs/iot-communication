package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspAcceptContent;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.*;


public class RtspDescribeRequestTest {

    @Test
    public void toObjectString() {
        String expect = "DESCRIBE * RTSP/1.0\r\n" +
                "CSeq:0\r\n" +
                "User-Agent:IOT-COMMUNICATION\r\n" +
                "Accept:application/sdp\r\n";
        URI uri = URI.create("*");
        RtspDescribeRequest request = new RtspDescribeRequest(uri, Collections.singletonList(ERtspAcceptContent.SDP));
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }
}