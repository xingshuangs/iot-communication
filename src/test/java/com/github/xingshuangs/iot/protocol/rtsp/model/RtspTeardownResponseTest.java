package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class RtspTeardownResponseTest {

    @Test
    public void fromString() {
        String src = "RTSP/1.0 200 OK\r\n" +
                "CSeq: 1\r\n" +
                "Session: 123456";

        RtspTeardownResponse response = RtspTeardownResponse.fromString(src);
        assertEquals(RtspMessage.VERSION_1_0, response.version);
        assertEquals(ERtspStatusCode.OK, response.statusCode);
        assertEquals(1, response.cSeq);
        assertEquals(123456, response.getSession());
    }
}