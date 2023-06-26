package com.github.xingshuangs.iot.protocol.rtsp.service;

import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Ignore
@Slf4j
public class RtspClientTest {

    @Test
    public void connectUdp() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        client.onCommCallback(log::info);
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            log.debug(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("{}", client.getTrackInfo());
            client.stop();
        });
        CompletableFuture<Void> future = client.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void connectTcp() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.TCP);
        client.onCommCallback(log::info);
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
//            log.debug(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("{}", client.getTrackInfo());
            client.stop();
        });
        CompletableFuture<Void> future = client.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void connectUdpWithoutAuthenticator() {
        List<H264VideoFrame> list = new ArrayList<>();
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        RtspClient client = new RtspClient(uri, ERtspTransportProtocol.UDP);
        client.onCommCallback(log::info);
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            list.add(f);
            H264VideoFrame tmp = null;
            if (list.size() >= 5) {
                list.sort((a, b) -> (int) (a.getTimestamp() - b.getTimestamp()));
                tmp = list.remove(0);
//                log.debug(f.getTimestamp() + ", " + HexUtil.toHexString(new byte[]{f.getFrameSegment()[0]}) + tmp.getTimestamp() + ", " + HexUtil.toHexString(new byte[]{tmp.getFrameSegment()[0]}));
            }
            if (tmp == null) {
                log.debug(f.getTimestamp() + ", " + HexUtil.toHexString(new byte[]{f.getFrameSegment()[0]}));
            } else {
                log.debug(f.getTimestamp() + ", " + HexUtil.toHexString(new byte[]{f.getFrameSegment()[0]}) + ", " + tmp.getTimestamp() + ", " + HexUtil.toHexString(new byte[]{tmp.getFrameSegment()[0]}));
            }
        });
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.stop();
        });
        CompletableFuture<Void> future = client.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}