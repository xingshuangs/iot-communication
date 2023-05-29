package com.github.xingshuangs.iot.protocol.rtsp.service;

import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Ignore
@Slf4j
public class RtspClientTest {

    @Test
    public void connect() {
//        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator);
        client.setCommCallback(x -> log.debug(x));
        client.setFrameHandle(x -> log.debug(x.getFrameType().toString()));
        client.connect();
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.disconnect();
        });
        client.receive();

    }

    @Test
    public void connect1() {
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        RtspClient client = new RtspClient(uri);
        client.setCommCallback(x -> log.debug(x));
        client.setFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            log.debug(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        client.connect();
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.disconnect();
        });
        client.receive();

    }
}