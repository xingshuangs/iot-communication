# RTSP/RTCP/RTP/H264/FMP4协议教程

[返回主页](../README-CN.md)

## 前言

- 目前支持RTSP + RTCP + RTP + FMP4 协议数据解析
- 目前支持H264视频流解析，主要针对摄像头
- 支持TCP/UDP传输协议
- 纯JAVA代码开发，没有任何其他依赖，轻量级
- 快速低延时，即开即用

## 示例

- 默认采用TCP的方式，也可以采用UDP的方式
- 支持用户名和密码方式的身份认证

### 1. RtspClient

```java
public class RtspTcpAuthenticator {

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
```

### 2. 构建FMP4

```java
public class DemoFMp4 {

    public static void main(String[] args) {
        List<Mp4SampleData> samples = new ArrayList<>();
        Mp4SampleData data = new Mp4SampleData();
        data.setTimestamp(System.currentTimeMillis());
        data.setData(new byte[5459]);
        data.setDuration(3600);
        data.setCts(0);
        Mp4SampleFlag flag = new Mp4SampleFlag();
        flag.setDependedOn(2);
        data.setFlags(flag);
        samples.add(data);

        Mp4TrackInfo trackInfo = new Mp4TrackInfo();
        trackInfo.setId(1);
        trackInfo.setType("video");
        trackInfo.setTimescale(90000);
        trackInfo.setDuration(90000);
        trackInfo.setWidth(1920);
        trackInfo.setHeight(1080);
        trackInfo.setSps(new byte[]{0x67, 0x64, 0x00, 0x2A, (byte) 0xAC, 0x2B, 0x50, 0x3C, 0x01, 0x13, (byte) 0xF2, (byte) 0xCD, (byte) 0xC0, 0x40, 0x40, 0x40, (byte) 0x80});
        trackInfo.setPps(new byte[]{0x68, (byte) 0xEE, 0x3C, (byte) 0xB0});
        trackInfo.setSampleData(samples);

        Mp4Header mp4FtypMoov = new Mp4Header(trackInfo);
        Mp4SampleData first = trackInfo.getSampleData().get(0);
        Mp4MoofBox mp4MoofBox = new Mp4MoofBox(1, first.getTimestamp(), trackInfo);
        Mp4MdatBox mp4MdatBox = new Mp4MdatBox(trackInfo.totalSampleData());
    }
}
```