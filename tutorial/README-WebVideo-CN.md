# WEB视频监控教程

[返回主页](../README-CN.md)

## 前言

- 采用**RTSP + H264 + FMP4 + WebSocket + MSE + WEB**的方案
- 目前支持**海康摄像头**RTSP视频流在WEB页面上显示，实现实时监控
- 视频流获取支持**TCP/UDP**两种方式，可根据网络情况任意切换
- 通讯库纯**JAVA**开发，没有任何其他依赖，轻量级
- 视频延时 **< 1s**，几乎无延时，实时性强
- 提供MSE+WebSocket相关js库代码

## 注意事项

- 浏览器必须支持**MSE**，**WebSocket**

## 示例

- 采用FMP4 + RTSP客户端的代理方式；
- 内部自动将H264视频流转换为FMP4格式；
- 默认采用TCP + 同步数据处理的方式，也支持UDP以及异步数据处理的方式

```java
public class RtspFMp4ProxyTcpSync {

    public static void main(String[] args) {
        // rtsp的摄像头地址
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // 身份认证
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // 构建RTSP客户端对象，此处采用TCP的通信方式
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.TCP);
        // 构建FMp4代理，此处采用同步方式
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client);
        // 设置FMP4数据接收事件
        proxy.onFmp4DataHandle(x -> {
            // *****编写处理数据业务*****
            System.out.println(x.length);
        });
        // 设置编解码格式数据事件
        proxy.onCodecHandle(x -> {
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
```

## 前端视频DEMO

- 可自行开发WebSocket的服务端，将视频流发送给WEB端
- 示例教程链接: https://github.com/xingshuangs/rtsp-websocket-server

![rtsp-websocket.png](https://i.postimg.cc/vBZzrGQB/img.png)
