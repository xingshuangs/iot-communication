# RTSP/RTCP/RTP/H264/FMP4 Protocol Tutorial

[HOME BACK](../README.md)

## Foreword

- Support RTSP + RTCP + RTP + FMP4 protocol data parsing.
- Support H264 video stream parsing, mainly for cameras.
- Support TCP/UDP transport protocol.
- Pure **JAVA** development, without any other dependencies, lightweight.
- Fast and low latency, out of the box.

## Example

- The default mode is TCP. You can also use UDP.
- Support username and password authentication.

### 1. RtspClient

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

### 2. Create FMP4

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