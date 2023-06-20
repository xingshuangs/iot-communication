# RTSP/RTCP/RTP/H264 Protocol Tutorial

[HOME BACK](../README.md)

## Foreword

- Support RTSP + RTCP + RTP protocol data parsing.
- Support H264 video stream parsing.
- Support TCP/UDP transport protocol.
- Pure **JAVA** development, without any other dependencies, lightweight.
- Fast and low latency, out of the box.

## Example

- The default mode is TCP. You can also use UDP.
- Supports username and password authentication.

### 1. TCP + NoAuthenticator Mode

```java
public class RtspTcpNoAuthenticator {

    public static void main(String[] args) {
        // rtsp address of camera
        URI uri = URI.create("rtsp://127.0.0.1:8554/11");
        // create the RTSP client instance, noAuthenticator + TCP
        RtspClient client = new RtspClient(uri);
        // set the RTSP interactive process information to print. If it is not required, do not set it.
        client.onCommCallback(System.out::println);
        // set the received video data frame event
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            System.out.println(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        // closed asynchronously, written before startup because it is a test example
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.stop();
        });
        // start, return asynchronous future
        CompletableFuture<Void> future = client.start();
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

### 2. TCP + Authenticator Mode

```java
public class RtspTcpAuthenticator {

    public static void main(String[] args) {
        // rtsp address of camera
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // identity authentication
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // create the RTSP client instance, authenticator + TCP
        RtspClient client = new RtspClient(uri, authenticator);
        // set the RTSP interactive process information to print. If it is not required, do not set it.
        client.onCommCallback(System.out::println);
        // set the received video data frame event
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            System.out.println(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        // closed asynchronously, written before startup because it is a test example
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.stop();
        });
        // start, return asynchronous future
        CompletableFuture<Void> future = client.start();
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

### 3. UDP + Authenticator Mode

```java
public class RtspUdpAuthenticator {

    public static void main(String[] args) {
        // rtsp address of camera
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // identity authentication
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // create the RTSP client instance, authenticator + UDP
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        // closed asynchronously, written before startup because it is a test example
        client.onCommCallback(System.out::println);
        // set the received video data frame event
        client.onFrameHandle(x -> {
            H264VideoFrame f = (H264VideoFrame) x;
            System.out.println(f.getFrameType() + ", " + f.getNaluType() + ", " + f.getTimestamp() + ", " + f.getFrameSegment().length);
        });
        // closed asynchronously, written before startup because it is a test example
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.stop();
        });
        // start, return asynchronous future
        CompletableFuture<Void> future = client.start();
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