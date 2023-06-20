package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xingshuang
 */
public class DemoRtspTcpAuthenticator {

    public static void main(String[] args) {
        // rtsp的摄像头地址
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // 身份认证
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // 构建RTSP客户端对象
        RtspClient client = new RtspClient(uri, authenticator);
        // 设置RTSP交互过程信息打印，若不需要则可以不设置
        client.onCommCallback(System.out::println);
        // 设置接收的视频数据帧事件
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            System.out.println(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        // 采用异步的形式关闭，由于是测试示例，写在启动前面
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.stop();
        });
        // 启动，返回异步执行对象
        CompletableFuture<Void> future = client.start();
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
