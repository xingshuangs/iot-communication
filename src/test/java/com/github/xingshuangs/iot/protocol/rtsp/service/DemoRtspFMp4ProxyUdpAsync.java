package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xingshuang
 */
public class DemoRtspFMp4ProxyUdpAsync {

    public static void main(String[] args) {
        // rtsp的摄像头地址
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // 身份认证
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // 构建RTSP客户端对象，此处采用UDP的通信方式
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        // 构建FMp4代理，此处采用异步方式
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client, true);
        // 设置FMP4数据接收事件
        proxy.onFmp4DataHandle(x -> {
            // *****编写处理数据业务*****
            System.out.println(x.length);
        });
        // 设置编解码格式数据事件
        proxy.onCodecHandle(x->{
            // *****编写处理数据业务*****
            System.out.println(x);
        });
        // 采用异步的形式关闭，由于是测试示例，写在启动前面
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        // 启动，返回异步执行对象
        CompletableFuture<Void> future = proxy.start();
        // 循环等待结束
        while (!future.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
