# Web Video Monitor Tutorial

[HOME BACK](../README.md)

## Foreword

- The scheme of **RTSP + H264 + FMP4 + WebSocket + MSE + WEB** is adopted.
- Support RTSP video stream of **Hikvision camera** display on the web page to achieve real-time monitoring.
- Video stream acquisition supports **TCP/UDP** two ways, which can be switched arbitrarily according to network
  conditions.
- Pure **JAVA** development, without any other dependencies, lightweight.
- Video delay **< 1s**, almost no delay, strong real-time.
- Provide **MSE + WebSocket** related js library code.

## Tips

- Browsers must support **MSE**，**WebSocket**.

## Example

- Using FMP4 + RTSP client agent mode.
- The H264 video stream is automatically converted to FMP4 format internally.
- By default, TCP + synchronous data processing is adopted, and UDP and asynchronous data processing are also supported.

```java
public class RtspFMp4ProxyTcpSync {

    public static void main(String[] args) {
        // rtsp address of camera
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // identity authentication
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // create the RTSP client instant, and use TCP communication mode here
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.TCP);
        // create the FMp4 proxy, here in synchronous mode
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client);
        // set FMP4 data reception event
        proxy.onFmp4DataHandle(x -> {
            // *****write processing data business*****
            System.out.println(x.length);
        });
        // sets codec format data events
        proxy.onCodecHandle(x -> {
            // *****write processing data business*****
            System.out.println(x);
        });
        // closed asynchronously, written before startup because it is a test example
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            proxy.stop();
        });
        // start, return asynchronous future
        CompletableFuture<Void> future = proxy.start();
        // loop wait end
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

## Front-end Video DEMO

- The WebSocket server can be developed by yourself, and send the video stream to the web.
- Example tutorial link: https://github.com/xingshuangs/rtsp-websocket-server.

![rtsp-websocket.png](https://i.postimg.cc/vBZzrGQB/img.png)