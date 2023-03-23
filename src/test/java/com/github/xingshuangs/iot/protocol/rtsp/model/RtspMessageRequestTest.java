package com.github.xingshuangs.iot.protocol.rtsp.model;

import org.junit.Test;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.CRLF;
import static org.junit.Assert.assertEquals;


public class RtspMessageRequestTest {

    @Test
    public void uriTest() {
        URI uri = URI.create("*");
        assertEquals("*", uri.toString());
        assertEquals("\r\n", CRLF);
    }

}