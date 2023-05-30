package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.net.ICommunicable;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpDataStatistics;
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
import com.github.xingshuangs.iot.protocol.rtsp.model.*;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspClientPortTransport;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRtpInfo;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspSessionInfo;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspTransport;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdp;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdpMedia;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private final Map<Integer, ICommunicable> socketClients = new HashMap<>();

    /**
     * 地址
     */
    private final URI uri;

    /**
     * 认证器
     */
    private DigestAuthenticator authenticator;

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
     * 数据收发前自定义处理接口
     */
    private Consumer<String> commCallback;

    private Consumer<RawFrame> frameHandle;

    private Consumer<byte[]> rtcpCallback;

    public void setCommCallback(Consumer<String> commCallback) {
        this.commCallback = commCallback;
    }

    public void setFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    public RtspNetwork(URI uri) {
        super(uri.getHost(), uri.getPort());
        this.uri = uri;
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator) {
        super(uri.getHost(), uri.getPort());
        this.uri = uri;
        this.authenticator = authenticator;
    }

    @Override
    public void close() {
        this.clearSocketConnection();
        super.close();
    }

    private RtspMessageResponse readFromServer(RtspMessageRequest req) {
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
    }

    /**
     * 设置
     */
    protected void setup() {
        this.checkBeforeRequest(ERtspMethod.SETUP);

        for (RtspSdpMedia media : this.sdp.getMedias()) {
            if (!media.getMediaDesc().getType().equals("video")) {
                continue;
            }
            // TODO: 这里可能存在不同的负载解析器
            IPayloadParser iPayloadParser = new H264VideoParser();
            RtpUdpClient rtpClient = new RtpUdpClient(iPayloadParser);
            rtpClient.setFrameHandle(this::doFrameHandle);
            RtcpDataStatistics statistics = new RtcpDataStatistics();
            RtcpUdpClient rtcpClient = new RtcpUdpClient(statistics);
            rtpClient.setRtcpUdpClient(rtcpClient);

            // 构建RtspTransport
            URI actualUri = URI.create(media.getAttributeControl().getUri());
            RtspClientPortTransport reqTransport = new RtspClientPortTransport();
            reqTransport.setProtocol(this.sdp.getMedias().get(0).getMediaDesc().getProtocol());
            reqTransport.setCastMode("unicast");
            reqTransport.setRtpClientPort(rtpClient.getLocalPort());
            reqTransport.setRtcpClientPort(rtcpClient.getLocalPort());

            // 发送Setup
            RtspSetupRequest request = this.needAuthorization ? new RtspSetupRequest(actualUri, reqTransport, this.authenticator)
                    : new RtspSetupRequest(actualUri, reqTransport);
            RtspSetupResponse response = (RtspSetupResponse) this.readFromServer(request);
            this.checkAfterResponse(response, ERtspMethod.SETUP);
            // 更新Transport和Session信息
            this.transport = response.getTransport();
            this.sessionInfo = response.getSessionInfo();

            RtspClientPortTransport ackTransport = (RtspClientPortTransport) this.transport;
            // socket客户端配置
            rtpClient.bindServer(this.uri.getHost(), ackTransport.getRtpServerPort());
            rtcpClient.bindServer(this.uri.getHost(), ackTransport.getRtcpServerPort());
            // 发送SPS和PPS
            if (this.frameHandle != null && media.getMediaDesc().getType().equals("video")) {
                this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getSps()));
                this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getPps()));
            }
            rtpClient.triggerReceive();
            rtcpClient.triggerReceive();

            // 设置成功后，存储一下
            this.socketClients.put(rtpClient.getLocalPort(), rtcpClient);
            this.socketClients.put(rtcpClient.getLocalPort(), rtcpClient);
        }
        // 为了保证RtpUdpClient和RtcpUdpClient的接收数据线程都开启
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
    }

    /**
     * 关闭
     */
    protected void teardown() {
        this.checkBeforeRequest(ERtspMethod.TEARDOWN);

        RtspTeardownRequest request = this.needAuthorization ? new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId())
                : new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator);
        RtspTeardownResponse response = (RtspTeardownResponse) this.readFromServer(request);

        this.checkAfterResponse(response, ERtspMethod.TEARDOWN);
        this.clearSocketConnection();
    }

    /**
     * 获取参数
     */
    protected void getParameter() {
        this.checkBeforeRequest(ERtspMethod.GET_PARAMETER);

        RtspGetParameterRequest request = this.needAuthorization ?
                new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId())
                : new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator);
        RtspGetParameterResponse response = (RtspGetParameterResponse) this.readFromServer(request);

        this.checkAfterResponse(response, ERtspMethod.GET_PARAMETER);
    }

    /**
     * 清空socket连接对象
     */
    private void clearSocketConnection() {
        if (this.socketClients.isEmpty()) {
            return;
        }
        this.socketClients.values().forEach(ICommunicable::close);
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

    private void doFrameHandle(RawFrame frame) {
        if (this.frameHandle != null) {
            this.frameHandle.accept(frame);
        }
    }
}
