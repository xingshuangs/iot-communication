package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpUdpClient;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import com.github.xingshuangs.iot.protocol.rtp.service.H264VideoParser;
import com.github.xingshuangs.iot.protocol.rtp.service.IPayloadParser;
import com.github.xingshuangs.iot.protocol.rtp.service.RtpUdpClient;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspAcceptContent;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspTransportProtocol;
import com.github.xingshuangs.iot.protocol.rtsp.model.*;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.*;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdp;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdpMedia;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspTrackInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;

/**
 * @author xingshuang
 */
@Slf4j
@Getter
public class RtspNetwork extends TcpClientBasic {
    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * 是否需要授权认证
     */
    private boolean needAuthorization = false;

    /**
     * socket客户端列表
     */
    private final Map<Integer, IRtspDataStream> socketClients = new HashMap<>();

    /**
     * 地址
     */
    protected final URI uri;

    /**
     * 认证器
     */
    protected DigestAuthenticator authenticator;

    /**
     * 支持的方法
     */
    protected List<ERtspMethod> methods = new ArrayList<>();

    /**
     * 会话描述协议
     */
    protected RtspSdp sdp;

    /**
     * 通道
     */
    protected RtspTransport transport;

    /**
     * RTP的信息
     */
    protected List<RtspRtpInfo> rtpInfos = new ArrayList<>();

    /**
     * session信息
     */
    protected RtspSessionInfo sessionInfo;

    /**
     * 轨道信息
     */
    protected RtspTrackInfo trackInfo;

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<String> commCallback;

    /**
     * 数据帧处理
     */
    private Consumer<RawFrame> frameHandle;

    /**
     * 通信协议，TCP、UDP
     */
    protected ERtspTransportProtocol transportProtocol;

    public RtspTrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setCommCallback(Consumer<String> commCallback) {
        this.commCallback = commCallback;
    }

    public void setFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    public RtspNetwork(URI uri) {
        this(uri, ERtspTransportProtocol.UDP);
    }

    public RtspNetwork(URI uri, ERtspTransportProtocol transportProtocol) {
        this(uri, null, transportProtocol);
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator) {
        this(uri, authenticator, ERtspTransportProtocol.UDP);
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator, ERtspTransportProtocol transportProtocol) {
        super(uri.getHost(), uri.getPort());
        // 不需要自动重连，连不上就停掉
        this.enableReconnect = false;
        this.uri = uri;
        this.authenticator = authenticator;
        this.transportProtocol = transportProtocol;
    }

    @Override
    protected void doAfterConnected() {
        this.option();
        this.describe();
        this.setup();
        this.play();
    }

    @Override
    public void close() {
        this.clearSocketConnection();
        super.close();
    }

    /**
     * 从服务器读取数据
     *
     * @param req 请求
     * @return 响应
     */
    public RtspMessageResponse readFromServer(RtspMessageRequest req) {
        String reqString = req.toObjectString();
        if (this.commCallback != null) {
            this.commCallback.accept(reqString);
        }
        byte[] reqBytes = reqString.getBytes(StandardCharsets.US_ASCII);

        int headerLen;
        String contentString = "";
        RtspMessageResponse ack;
        synchronized (this.objLock) {
            this.write(reqBytes);
            // 读取并解析头
            byte[] header = new byte[1024];
            headerLen = this.read(header);
            contentString = new String(header, 0, headerLen, StandardCharsets.US_ASCII);
            ack = RtspMessageResponseBuilder.fromString(contentString, req);
            // 读取并解析body
            if (ack.getContentLength() != null && ack.getContentLength() > 0) {
                // 是否包含内容体，通过两个换行符来判定，如果没有就再读一次，摄像头和VLC的情况不一样
                String bodyString = "";
                int i = contentString.indexOf(CRLF + CRLF);
                if (contentString.length() > i + 5) {
                    bodyString = contentString.substring(i + 4);
                } else {
                    byte[] body = new byte[ack.getContentLength()];
                    this.read(body);
                    bodyString = new String(body, StandardCharsets.US_ASCII);
                    contentString += bodyString;
                }
                if (!bodyString.isEmpty()) {
                    ack.addBodyFromString(bodyString);
                }
            }
        }
        if (headerLen == 0) {
            throw new RtspCommException("RTSP数据接收长度为0，错误");
        }
        if (this.commCallback != null) {
            this.commCallback.accept(contentString);
        }
        this.checkPostedCom(req, ack);
        return ack;
    }

    /**
     * 发送数据给服务器，但不返回
     *
     * @param req 请求数据
     */
    public void sendToServer(RtspMessageRequest req) {
        String reqString = req.toObjectString();
        if (this.commCallback != null) {
            this.commCallback.accept(reqString);
        }
        byte[] reqBytes = reqString.getBytes(StandardCharsets.US_ASCII);
        // 后续的发送不需要加锁，因为read不再接收响应
        this.write(reqBytes);
    }

    /**
     * 通信后置校验
     *
     * @param req 请求
     * @param ack 响应
     */
    private void checkPostedCom(RtspMessageRequest req, RtspMessageResponse ack) {
        if (!req.getVersion().equals(ack.getVersion())) {
            throw new RtspCommException("请求和响应的版本号不一致");
        }
        if (req.getCSeq() != ack.getCSeq()) {
            throw new RtspCommException("请求和响应的序列号不一致");
        }
    }

    /**
     * 选项
     */
    protected void option() {
        RtspOptionRequest request = new RtspOptionRequest(this.uri);
        RtspOptionResponse response = (RtspOptionResponse) this.readFromServer(request);
        this.checkAfterResponse(response, ERtspMethod.OPTIONS);
        this.methods = response.getPublicMethods();
        // 清空客户端
        this.clearSocketConnection();
    }

    /**
     * 描述
     */
    protected void describe() {
        this.checkBeforeRequest(ERtspMethod.DESCRIBE);

        RtspDescribeRequest request = new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP));
        RtspDescribeResponse response = (RtspDescribeResponse) this.readFromServer(request);

        if (response.getStatusCode() == ERtspStatusCode.UNAUTHORIZED) {
            // 需要授权
            this.needAuthorization = true;
            if (this.authenticator == null) {
                throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", ERtspMethod.DESCRIBE));
            }
            this.authenticator.addServerInfoByString(response.getWwwAuthenticate());
            this.authenticator.addClientInfo(this.uri.toString(), ERtspMethod.DESCRIBE.getCode());
            request = new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP), this.authenticator);
            response = (RtspDescribeResponse) this.readFromServer(request);
        }

        this.checkAfterResponse(response, ERtspMethod.DESCRIBE);

        if (response.getSdp().getSession() == null) {
            throw new RtspCommException(String.format("RTSP[%s]没有Session", ERtspMethod.DESCRIBE));
        }
        if (response.getSdp().getMedias().isEmpty()) {
            throw new RtspCommException(String.format("RTSP[%s]没有Media", ERtspMethod.DESCRIBE));
        }
        this.sdp = response.getSdp();
        this.trackInfo = RtspTrackInfo.createTrackInfo(this.sdp);
    }

    /**
     * 设置
     */
    protected void setup() {
        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
            this.setupUdp();
        } else {
            this.setupTcp();
        }
    }

    /**
     * UDP设置
     */
    private void setupUdp() {
        for (RtspSdpMedia media : this.sdp.getMedias()) {
            if (!media.getMediaDesc().getType().equals("video")) {
                continue;
            }
            // TODO: 这里可能存在不同的负载解析器
            IPayloadParser iPayloadParser = new H264VideoParser();
            iPayloadParser.setFrameHandle(this::doFrameHandle);
            URI actualUri = URI.create(media.getAttributeControl().getUri());
            RtpUdpClient rtpClient = new RtpUdpClient(iPayloadParser);
            RtcpUdpClient rtcpClient = new RtcpUdpClient();
            rtpClient.setRtcpUdpClient(rtcpClient);
            RtspTransport reqTransport = new RtspClientPortTransport(rtpClient.getLocalPort(), rtcpClient.getLocalPort());

            // 发送Setup
            this.doSetup(actualUri, reqTransport, media);

            RtspClientPortTransport ackTransport = (RtspClientPortTransport) this.transport;
            // socket客户端配置
            rtpClient.bindServer(this.uri.getHost(), ackTransport.getRtpServerPort());
            rtcpClient.bindServer(this.uri.getHost(), ackTransport.getRtcpServerPort());

            // 设置成功后，存储一下
            this.socketClients.put(rtpClient.getLocalPort(), rtpClient);
            this.socketClients.put(rtcpClient.getLocalPort(), rtcpClient);
        }
    }

    /**
     * TCP设置
     */
    private void setupTcp() {
        int interleavedCount = 0;
        for (RtspSdpMedia media : this.sdp.getMedias()) {
            if (!media.getMediaDesc().getType().equals("video")) {
                continue;
            }
            // TODO: 这里可能存在不同的负载解析器
            int rtpChannelNumber = interleavedCount++;
            int rtcpChannelNumber = interleavedCount++;
            RtspTransport reqTransport = new RtspInterleavedTransport(rtpChannelNumber, rtcpChannelNumber);
            URI actualUri = URI.create(media.getAttributeControl().getUri());

            this.doSetup(actualUri, reqTransport, media);

            IPayloadParser iPayloadParser = new H264VideoParser();
            iPayloadParser.setFrameHandle(this::doFrameHandle);
            RtspInterleavedTransport ackTransport = (RtspInterleavedTransport) this.transport;
            RtspInterleavedClient rtspInterleavedClient = new RtspInterleavedClient(iPayloadParser, this);
            rtspInterleavedClient.setRtpVideoChannelNumber(ackTransport.getInterleaved1());
            rtspInterleavedClient.setRtcpVideoChannelNumber(ackTransport.getInterleaved2());
            this.socketClients.put(rtspInterleavedClient.getRtpVideoChannelNumber(), rtspInterleavedClient);
        }
    }

    /**
     * 正在执行设置
     *
     * @param actualUri    实际URI
     * @param reqTransport transport
     * @param media        媒体信息
     */
    private void doSetup(URI actualUri, RtspTransport reqTransport, RtspSdpMedia media) {
        this.checkBeforeRequest(ERtspMethod.SETUP);
        // 发送Setup
        RtspSetupRequest request = this.needAuthorization ? new RtspSetupRequest(actualUri, reqTransport, this.authenticator)
                : new RtspSetupRequest(actualUri, reqTransport);
        RtspSetupResponse response = (RtspSetupResponse) this.readFromServer(request);
        this.checkAfterResponse(response, ERtspMethod.SETUP);
        // 更新Transport和Session信息
        this.transport = response.getTransport();
        this.sessionInfo = response.getSessionInfo();

        // 发送SPS和PPS
        if (this.frameHandle != null && media.getMediaDesc().getType().equals("video")) {
            this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getSps()));
            this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getPps()));
        }
    }

    /**
     * 播放
     */
    protected void play() {
        this.checkBeforeRequest(ERtspMethod.PLAY);

        RtspPlayRequest request = this.needAuthorization ? new RtspPlayRequest(this.uri, this.sessionInfo.getSessionId())
                : new RtspPlayRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator);
        RtspPlayResponse response = (RtspPlayResponse) this.readFromServer(request);
        this.checkAfterResponse(response, ERtspMethod.PLAY);

        this.rtpInfos = response.getRtpInfo();
        // play命令之后触发接收数据
        this.socketClients.values().forEach(IRtspDataStream::triggerReceive);
    }

    /**
     * 关闭
     */
    protected void teardown() {
        this.clearSocketConnection();
        this.checkBeforeRequest(ERtspMethod.TEARDOWN);

        RtspTeardownRequest request = this.needAuthorization ? new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId())
                : new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator);
        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
            RtspTeardownResponse response = (RtspTeardownResponse) this.readFromServer(request);
            this.checkAfterResponse(response, ERtspMethod.TEARDOWN);
        } else {
            this.sendToServer(request);
        }
    }

    /**
     * 获取参数
     */
    protected void getParameter() {
        this.checkBeforeRequest(ERtspMethod.GET_PARAMETER);

        RtspGetParameterRequest request = this.needAuthorization ?
                new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId())
                : new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator);
        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
            RtspGetParameterResponse response = (RtspGetParameterResponse) this.readFromServer(request);
            this.checkAfterResponse(response, ERtspMethod.GET_PARAMETER);
        } else {
            this.sendToServer(request);
        }
    }

    /**
     * 清空socket连接对象
     */
    private void clearSocketConnection() {
        if (this.socketClients.isEmpty()) {
            return;
        }
        this.socketClients.values().forEach(IRtspDataStream::close);
        this.socketClientJoinForFinished();
        this.socketClients.clear();
    }

    /**
     * 请求前数据校验
     *
     * @param method 方法
     */
    private void checkBeforeRequest(ERtspMethod method) {
        if (!methods.contains(method)) {
            throw new RtspCommException(String.format("RTSP不支持[%s]", method.getCode()));
        }
        if (this.needAuthorization && this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", method.getCode()));
        }
    }

    /**
     * 响应后的数据校验
     *
     * @param response 响应
     * @param method   方法
     */
    private void checkAfterResponse(RtspMessageResponse response, ERtspMethod method) {
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", method.getCode(), response.getStatusCode().getCode()));
        }
    }

    /**
     * 处理帧数据
     *
     * @param frame 帧数据
     */
    private void doFrameHandle(RawFrame frame) {
        if (this.frameHandle != null) {
            this.frameHandle.accept(frame);
        }
    }

    /**
     * 等待所有socket客户端结束
     */
    protected void socketClientJoinForFinished() {
        CompletableFuture<?>[] futures = this.socketClients.values().stream()
                .map(IRtspDataStream::getFuture)
                .toArray(CompletableFuture[]::new);
        CompletableFuture<Void> all = CompletableFuture.allOf(futures);
        all.join();
    }

    /**
     * socket客户端线程是否都已经结束
     *
     * @return true：结束，false：未结束
     */
    protected boolean socketClientIsAllDone() {
        return this.socketClients.values().stream()
                .map(IRtspDataStream::getFuture)
                .allMatch(CompletableFuture::isDone);
    }
}
