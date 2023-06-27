package com.github.xingshuangs.iot.protocol.rtsp.service;

import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Ignore
public class RtspFMp4ProxyTest {

    @Test
    public void runTcpSync1() {
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        RtspClient client = new RtspClient(uri, ERtspTransportProtocol.UDP);
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client);
        proxy.onFmp4DataHandle(x -> {
            log.debug("length：{}", x.length);
        });
        proxy.onCodecHandle(log::debug);
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        CompletableFuture<Void> future = proxy.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runTcpSync() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.TCP);
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client);
        proxy.onFmp4DataHandle(x -> {
            log.debug("length：{}", x.length);
        });
        proxy.onCodecHandle(log::debug);
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        CompletableFuture<Void> future = proxy.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runTcpAsync() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.TCP);
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client, true);
        proxy.onFmp4DataHandle(x -> {
            log.debug("length：{}", x.length);
        });
        proxy.onCodecHandle(log::debug);
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        CompletableFuture<Void> future = proxy.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runUdpSync() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client, false);
        proxy.onFmp4DataHandle(x -> {
            log.debug("length：{}", x.length);
        });
        proxy.onCodecHandle(log::debug);
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        CompletableFuture<Void> future = proxy.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runUdpAsync() {
        URI uri = URI.create("rtsp://192.168.3.142:554/h264/ch1/main/av_stream");
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client, true);
        proxy.onFmp4DataHandle(x -> {
            log.debug("length：{}", x.length);
        });
        proxy.onCodecHandle(log::debug);
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        CompletableFuture<Void> future = proxy.start();
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}