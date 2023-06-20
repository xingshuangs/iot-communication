# Web Video Monitor Tutorial

[HOME BACK](../README.md)

## Foreword

- The scheme of **RTSP + H264 + FMP4 + WebSocket + MSE + WEB** is adopted.
- Support RTSP video stream of **Hikvision camera** display on the web page to achieve real-time monitoring.
- Video stream acquisition supports **TCP/UDP** two ways, which can be switched arbitrarily according to network conditions.
- Pure **JAVA** development, without any other dependencies, lightweight.
- Video delay **< 1s**, almost no delay, strong real-time.
- The WebSocket server can be developed by yourself, and send the video stream to the web, example tutorial link: https://github.com/xingshuangs/rtsp-websocket-server.
- Provide **MSE + WebSocket** related js library code.

## Tips

- Browsers must support **MSE**，**WebSocket**.

## Example

- Using FMP4 + RTSP client agent mode.
- The H264 video stream is automatically converted to FMP4 format internally.
- By default, TCP + synchronous data processing is adopted, and UDP and asynchronous data processing are also supported.

### 1. TCP + Sync Mode

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
        proxy.onCodecHandle(x->{
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

### 2. UDP + Async Mode

```java
public class RtspFMp4ProxyUdpAsync {

    public static void main(String[] args) {
        // rtsp address of camera
        URI uri = URI.create("rtsp://192.168.3.1:554/h264/ch1/main/av_stream");
        // identity authentication
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        // create the RTSP client instant, and use UDP communication mode here
        RtspClient client = new RtspClient(uri, authenticator, ERtspTransportProtocol.UDP);
        // create the FMp4 proxy, here in asynchronous mode
        RtspFMp4Proxy proxy = new RtspFMp4Proxy(client, true);
        // set FMP4 data reception event
        proxy.onFmp4DataHandle(x -> {
            // *****write processing data business*****
            System.out.println(x.length);
        });
        // sets codec format data events
        proxy.onCodecHandle(x->{
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

## Front-end adapted js library

- This code is JavaScript code, not JAVA code.
- Services related to WebSocket + MSE interaction have been implemented.
- Just set wsUrl(WebSocket address), rtspUrl(rtsp address), videoId(video tag Id), and then open.

```javascript
class RtspStream {

    constructor(wsUrl, rtspUrl, videoId) {
        this.wsUrl = wsUrl;
        this.rtspUrl = rtspUrl;
        this.videoId = videoId;
        this.queue = [];
        this.canFeed = false;
    }

    onopen(evt) {
        console.log("ws open")
        this.websocket.send(this.rtspUrl)
    }

    onClose(evt) {
        console.log("ws close")
    }

    onMessage(evt) {
        if (typeof (evt.data) == "string") {
            this.init(evt.data)
        } else {
            this.queue.push(new Uint8Array(evt.data));
            if (this.canFeed) {
                this.feedNext();
            }
        }
    }

    onError(evt) {
        console.log("ws error")
    }

    open() {
        this.websocket = new WebSocket(this.wsUrl);
        this.websocket.binaryType = "arraybuffer";
        this.websocket.onopen = this.onopen.bind(this);
        this.websocket.onmessage = this.onMessage.bind(this);
        this.websocket.onclose = this.onClose.bind(this);
        this.websocket.onerror = this.onError.bind(this);
    }

    close() {
        this.websocket.close();
    }

    /**
     * init
     * @param codecStr codec string
     */
    init(codecStr) {
        this.codec = 'video/mp4; codecs=\"' + codecStr + '\"';
        console.log("call play:", this.codec);
        if (MediaSource.isTypeSupported(this.codec)) {
            this.mediaSource = new MediaSource;
            this.mediaSource.addEventListener('sourceopen', this.onMediaSourceOpen.bind(this));
            this.mediaPlayer = document.getElementById(this.videoId);
            this.mediaPlayer.src = URL.createObjectURL(this.mediaSource);
        } else {
            console.log("Unsupported MIME type or codec: ", +this.codec);
        }
    }

    /**
     * MediaSource opened
     * @param e event
     */
    onMediaSourceOpen(e) {
        // URL.revokeObjectURL active release reference
        URL.revokeObjectURL(this.mediaPlayer.src);
        this.mediaSource.removeEventListener('sourceopen', this.onMediaSourceOpen.bind(this));

        this.sourceBuffer = this.mediaSource.addSourceBuffer(this.codec);
        this.sourceBuffer.addEventListener('about', e => console.log(`about `, e));
        this.sourceBuffer.addEventListener('error', e => console.log(`error `, e));
        this.sourceBuffer.addEventListener('updateend', e => this.feedNext());
        this.canFeed = true;
    }

    /**
     * feed
     */
    feedNext() {
        if (!this.queue || !this.queue.length) return

        if (this.sourceBuffer && !this.sourceBuffer.updating) {
            const data = this.queue.shift();
            this.sourceBuffer.appendBuffer(data);
        }
    }
}
```