/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpUdpClient;
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
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute.RtspSdpMediaAttrRtpMap;
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
     * locker
     */
    private final Object objLock = new Object();

    /**
     * If need authorization.
     * (是否需要授权认证)
     */
    private boolean needAuthorization = false;

    /**
     * Socket clients.
     * (socket客户端列表)
     */
    private final Map<Integer, IRtspDataStream> socketClients = new HashMap<>();

    /**
     * Uri
     * (地址)
     */
    protected final URI uri;

    /**
     * Authenticator.
     * (认证器)
     */
    protected DigestAuthenticator authenticator;

    /**
     * Supported methods.
     * (支持的方法)
     */
    protected List<ERtspMethod> methods = new ArrayList<>();

    /**
     * SDP
     * (会话描述协议)
     */
    protected RtspSdp sdp;

    /**
     * Transport.
     * (通道)
     */
    protected RtspTransport transport;

    /**
     * RTP infos.
     * (RTP的信息)
     */
    protected List<RtspRtpInfo> rtpInfos = new ArrayList<>();

    /**
     * Session info.
     * (session信息)
     */
    protected RtspSessionInfo sessionInfo;

    /**
     * Track info.
     * (轨道信息)
     */
    protected RtspTrackInfo trackInfo;

    /**
     * Communication callback.
     * (数据收发前自定义处理接口)
     */
    private Consumer<String> commCallback;

    /**
     * Frame handle.
     * (数据帧处理)
     */
    private Consumer<RawFrame> frameHandle;

    /**
     * Destroy handle.
     * (销毁的处理事件)
     */
    private Runnable destroyHandle;

    /**
     * Transport protocol.
     * (通信协议，TCP、UDP)
     */
    protected ERtspTransportProtocol transportProtocol;

    public void onCommCallback(Consumer<String> commCallback) {
        this.commCallback = commCallback;
    }

    public void onFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    public void onDestroyHandle(Runnable destroyHandle) {
        this.destroyHandle = destroyHandle;
    }

    public RtspNetwork(URI uri) {
        this(uri, ERtspTransportProtocol.TCP);
    }

    public RtspNetwork(URI uri, ERtspTransportProtocol transportProtocol) {
        this(uri, null, transportProtocol);
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator) {
        this(uri, authenticator, ERtspTransportProtocol.TCP);
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator, ERtspTransportProtocol transportProtocol) {
        super(uri.getHost(), uri.getPort());
        // 不需要自动重连，连不上就停掉
        this.enableReconnect = false;
        this.uri = uri;
        this.authenticator = authenticator;
        this.transportProtocol = transportProtocol;
        this.tag = "RTSP";
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
     * Read form server.
     * (从服务器读取数据)
     *
     * @param req request
     * @return response
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
            byte[] header = new byte[4096];
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
            throw new RtspCommException("RTSP data receive length is 0");
        }
        if (this.commCallback != null) {
            this.commCallback.accept(contentString);
        }
        this.checkPostedCom(req, ack);
        return ack;
    }

    /**
     * Send data to server without return.
     * (发送数据给服务器，但不返回)
     *
     * @param req request
     */
    public void sendWithoutReturn(RtspMessageRequest req) {
        if (this.needAuthorization && this.authenticator != null) {
            this.authenticator.setMethod(req.getMethod().getCode());
        }
        String reqString = req.toObjectString();
        if (this.commCallback != null) {
            this.commCallback.accept(reqString);
        }
        byte[] reqBytes = reqString.getBytes(StandardCharsets.US_ASCII);
        // 后续的发送不需要加锁，因为发送数据后不再接收响应
        this.write(reqBytes);
    }

    /**
     * Check after communicate.
     * (通信后置校验)
     *
     * @param req request
     * @param ack response
     */
    private void checkPostedCom(RtspMessageRequest req, RtspMessageResponse ack) {
        if (!req.getVersion().equals(ack.getVersion())) {
            // 请求和响应的版本号不一致
            throw new RtspCommException("The version numbers of the request and response are inconsistent");
        }
        if (req.getCSeq() != ack.getCSeq()) {
            // 请求和响应的序列号不一致
            throw new RtspCommException("The sequence numbers of the request and response are inconsistent");
        }
    }

    /**
     * Send request.
     * (发送对应请求)
     *
     * @param request request
     * @return response
     */
    private RtspMessageResponse sendRequest(RtspMessageRequest request) {
        if (this.needAuthorization && this.authenticator != null) {
            this.authenticator.setMethod(request.getMethod().getCode());
        }
        RtspMessageResponse response = this.readFromServer(request);
        if (response.getStatusCode() == ERtspStatusCode.UNAUTHORIZED) {
            // 需要授权
            this.needAuthorization = true;
            if (this.authenticator == null) {
                throw new RtspCommException(String.format("RTSP[%s] authenticator is null", request.getMethod()));
            }
            this.authenticator.addServerInfoByString(response.getWwwAuthenticate());
            this.authenticator.addClientInfo(this.uri.toString(), request.getMethod().getCode());
            request.setAuthenticator(this.authenticator);
            response = this.readFromServer(request);
        }
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s] status code is [%s]", request.getMethod().getCode(),
                    response.getStatusCode().getCode()));
        }
        return response;
    }

    /**
     * Option method operation.
     */
    protected void option() {
        RtspOptionRequest request = new RtspOptionRequest(this.uri);
        RtspOptionResponse response = (RtspOptionResponse) this.sendRequest(request);
        this.methods = response.getPublicMethods();
        // 清空客户端
        this.clearSocketConnection();
    }

    /**
     * Describe method operation.
     */
    protected void describe() {
        this.checkBeforeRequest(ERtspMethod.DESCRIBE);

        RtspDescribeRequest request = this.needAuthorization ?
                new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP), this.authenticator)
                : new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP));
        RtspDescribeResponse response = (RtspDescribeResponse) this.sendRequest(request);

        if (response.getSdp().getSession() == null) {
            throw new RtspCommException(String.format("RTSP[%s] no Session", ERtspMethod.DESCRIBE));
        }
        if (response.getSdp().getMedias().isEmpty()) {
            throw new RtspCommException(String.format("RTSP[%s] no Media", ERtspMethod.DESCRIBE));
        }
        this.sdp = response.getSdp();
        this.trackInfo = RtspTrackInfo.createTrackInfo(this.sdp);
    }

    /**
     * Setup method operation.
     */
    protected void setup() {
        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
            this.setupUdp();
        } else {
            this.setupTcp();
        }
    }

    /**
     * UDP setup method operation.
     */
    private void setupUdp() {
        for (RtspSdpMedia media : this.sdp.getMedias()) {
            if (!media.getMediaDesc().getType().equals("video")) {
                continue;
            }
            // TODO: 这里可能存在不同的负载解析器
            RtspSdpMediaAttrRtpMap rtpMap = media.getAttributeRtpMap();
            IPayloadParser iPayloadParser = new H264VideoParser(rtpMap.getPayloadNumber());
            iPayloadParser.onFrameHandle(this::doFrameHandle);
            URI actualUri = URI.create(this.uri.toString() + "/" + media.getAttributeControl().getUri());
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
     * TCP setup method operation.
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
            URI actualUri = URI.create(this.uri.toString() + "/" + media.getAttributeControl().getUri());

            this.doSetup(actualUri, reqTransport, media);

            RtspSdpMediaAttrRtpMap rtpMap = media.getAttributeRtpMap();
            IPayloadParser iPayloadParser = new H264VideoParser(rtpMap.getPayloadNumber());
            iPayloadParser.onFrameHandle(this::doFrameHandle);
            RtspInterleavedTransport ackTransport = (RtspInterleavedTransport) this.transport;
            RtspInterleavedClient rtspInterleavedClient = new RtspInterleavedClient(iPayloadParser, this);
            rtspInterleavedClient.setRtpVideoChannelNumber(ackTransport.getInterleaved1());
            rtspInterleavedClient.setRtcpVideoChannelNumber(ackTransport.getInterleaved2());
            this.socketClients.put(rtspInterleavedClient.getRtpVideoChannelNumber(), rtspInterleavedClient);
        }
    }

    /**
     * Do setup.
     *
     * @param actualUri    actual URI
     * @param reqTransport transport
     * @param media        media info
     */
    private void doSetup(URI actualUri, RtspTransport reqTransport, RtspSdpMedia media) {
        this.checkBeforeRequest(ERtspMethod.SETUP);
        // 发送Setup
        RtspSetupRequest request = this.needAuthorization ?
                new RtspSetupRequest(actualUri, reqTransport, this.authenticator)
                : new RtspSetupRequest(actualUri, reqTransport);
        RtspSetupResponse response = (RtspSetupResponse) this.sendRequest(request);
        // 更新Transport和Session信息
        this.transport = response.getTransport();
        this.sessionInfo = response.getSessionInfo();

        // 发送SPS和PPS
//        if (this.frameHandle != null && media.getMediaDesc().getType().equals("video")) {
//            this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getSps()));
//            this.frameHandle.accept(H264VideoFrame.createSpsPpsFrame(media.getAttributeFmtp().getPps()));
//        }
    }

    /**
     * Play method operation.
     */
    protected void play() {
        this.checkBeforeRequest(ERtspMethod.PLAY);

        RtspPlayRequest request = this.needAuthorization ?
                new RtspPlayRequest(this.uri, this.sessionInfo.getSessionId(), new RtspRangeNpt("0.000"), this.authenticator)
                : new RtspPlayRequest(this.uri, this.sessionInfo.getSessionId(), new RtspRangeNpt("0.000"));
        RtspPlayResponse response = (RtspPlayResponse) this.sendRequest(request);

        this.rtpInfos = response.getRtpInfo();
        // play命令之后触发接收数据
        this.socketClients.values().forEach(IRtspDataStream::triggerReceive);
    }

    /**
     * Teardown method operation.
     */
    protected void teardown() {
        this.clearSocketConnection();
        this.checkBeforeRequest(ERtspMethod.TEARDOWN);

        RtspTeardownRequest request = this.needAuthorization ?
                new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator)
                : new RtspTeardownRequest(this.uri, this.sessionInfo.getSessionId());
//        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
//            RtspTeardownResponse response = (RtspTeardownResponse) this.readFromServer(request);
//            this.checkAfterResponse(response, ERtspMethod.TEARDOWN);
//        } else {
//            this.sendToServer(request);
//        }
        this.sendWithoutReturn(request);
        if (this.destroyHandle != null) {
            this.destroyHandle.run();
        }
    }

    /**
     * Get parameter method operation.
     */
    protected void getParameter() {
        this.checkBeforeRequest(ERtspMethod.GET_PARAMETER);

        RtspGetParameterRequest request = this.needAuthorization ?
                new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId(), this.authenticator)
                : new RtspGetParameterRequest(this.uri, this.sessionInfo.getSessionId());
        if (this.transportProtocol == ERtspTransportProtocol.UDP) {
            this.sendRequest(request);
        } else {
            this.sendWithoutReturn(request);
        }
    }

    /**
     * Clear socket connection.
     * (清空socket连接对象)
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
     * Check before request.
     * (请求前数据校验)
     *
     * @param method method
     */
    private void checkBeforeRequest(ERtspMethod method) {
        if (!methods.contains(method)) {
            throw new RtspCommException(String.format("RTSP nonsupport [%s]", method.getCode()));
        }
        if (this.needAuthorization && this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s] authenticator is null", method.getCode()));
        }
    }

    /**
     * Do frame handle
     * (处理帧数据)
     *
     * @param frame frame data
     */
    private void doFrameHandle(RawFrame frame) {
        if (this.frameHandle != null) {
            this.frameHandle.accept(frame);
        }
    }

    /**
     * Wait for all socket close.
     * (等待所有socket客户端结束)
     */
    protected void socketClientJoinForFinished() {
        CompletableFuture<?>[] futures = this.socketClients.values().stream()
                .map(IRtspDataStream::getFuture)
                .filter(Objects::nonNull)
                .toArray(CompletableFuture[]::new);
        if (futures.length == 0) {
            return;
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(futures);
        all.join();
    }

    /**
     * Is socket clients all closed.
     * (socket客户端线程是否都已经结束)
     *
     * @return true：finished，false：not finished.
     */
    protected boolean socketClientIsAllDone() {
        return this.socketClients.values().stream()
                .map(IRtspDataStream::getFuture)
                .allMatch(CompletableFuture::isDone);
    }
}
