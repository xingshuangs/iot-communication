package com.github.xingshuangs.iot.protocol.rtsp.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

@Ignore
@Slf4j
public class RtspClientTest {

    @Test
    public void connect() {
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        RtspClient client = new RtspClient(uri);
        client.setCommCallback(x -> log.debug(x));
        client.connect();
        client.receive();
    }
}