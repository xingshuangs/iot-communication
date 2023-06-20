# RTSP/RTCP/RTP/H264协议教程

[返回主页](../README-CN.md)

## 前言

- 目前支持RTSP + RTCP + RTP协议数据解析
- 目前支持H264视频流解析
- 支持TCP/UDP传输协议
- 纯JAVA代码开发，没有任何其他依赖，轻量级
- 快速低延时，即开即用

## 示例

- 默认采用TCP的方式，也可以采用UDP的方式
- 支持用户名和密码方式的身份认证

### 1. TCP + 无身份认证方式

```java
public class RtspTcpNoAuthenticator {

    public static void main(String[] args) {
        // rtsp的地址
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        // 构建RTSP客户端对象
        RtspClient client = new RtspClient(uri);
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

### 2. TCP + 有身份认证方式

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

### 3. UDP + 有身份认证方式

```java
public class RtspUdpAuthenticator {

    public static void main(String[] args) {
        // rtsp的摄像头地址
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // 身份认证
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // 构建RTSP客户端对象
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
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