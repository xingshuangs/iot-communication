package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspAcceptContent;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.protocol.rtsp.model.*;
import com.github.xingshuangs.iot.protocol.rtsp.sdp.RtspSdp;
import com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute.RtspSdpMediaAttrControl;
import lombok.Getter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xingshuang
 */
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
    private List<ERtspMethod> methods = new ArrayList<>();

    /**
     * 会话描述协议
     */
    private RtspSdp sdp;

    /**
     * 通道
     */
    private RtspTransport transport;

    /**
     * RTP的信息
     */
    private List<RtspRtpInfo> rtpInfos = new ArrayList<>();

    /**
     * sessionId
     */
    private Integer session;

    public RtspNetwork(URI uri) {
        super(uri.getHost(), uri.getPort());
        this.uri = uri;
    }

    public RtspNetwork(URI uri, DigestAuthenticator authenticator) {
        super(uri.getHost(), uri.getPort());
        this.uri = uri;
        this.authenticator = authenticator;
    }

    private RtspMessageResponse readFromServer(RtspMessageRequest req) {
        String reqString = req.toObjectString();
        byte[] reqBytes = reqString.getBytes(StandardCharsets.US_ASCII);

        int len;
        byte[] data;
        synchronized (this.objLock) {
            this.write(reqBytes);
            data = new byte[1024];
            len = this.read(data);
        }
        if (len == 0) {
            throw new RtspCommException("RTSP数据接收长度为0，错误");
        }
        byte[] ackBytes = new byte[len];
        System.arraycopy(data, 0, ackBytes, 0, len);
        RtspMessageResponse ack = RtspMessageResponseBuilder.fromString(new String(ackBytes, StandardCharsets.US_ASCII), req);
        this.checkPostedCom(req, ack);
        return ack;
    }

    private void checkPostedCom(RtspMessageRequest req, RtspMessageResponse ack) {
        if (!req.getVersion().equals(ack.getVersion())) {
            throw new RtspCommException("请求和响应的版本号不一致");
        }
        if (req.getCSeq() != ack.getCSeq()) {
            throw new RtspCommException("请求和响应的序列号不一致");
        }
    }

    protected void option() {
        RtspOptionRequest request = new RtspOptionRequest(this.uri);
        RtspOptionResponse response = (RtspOptionResponse) this.readFromServer(request);
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.OPTIONS, response.getStatusCode().getCode()));
        }
        this.methods = response.getPublicMethods();
    }

    protected void describe() {
        if (!methods.contains(ERtspMethod.DESCRIBE)) {
            throw new RtspCommException("RTSP不支持[DESCRIBE]");
        }
        RtspDescribeRequest request = new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP));
        RtspDescribeResponse response = (RtspDescribeResponse) this.readFromServer(request);
        if (response.getStatusCode() == ERtspStatusCode.OK) {
            if (response.getSdp().getSession() == null) {
                throw new RtspCommException(String.format("RTSP[%s]没有Session", ERtspMethod.DESCRIBE));
            }
            if (!response.getSdp().getMedias().isEmpty()) {
                throw new RtspCommException(String.format("RTSP[%s]没有Media", ERtspMethod.DESCRIBE));
            }
            this.sdp = response.getSdp();
        }
        if (response.getStatusCode() != ERtspStatusCode.UNAUTHORIZED) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.DESCRIBE, response.getStatusCode().getCode()));
        }
        // 需要授权
        this.needAuthorization = true;
        if (this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", ERtspMethod.DESCRIBE));
        }
        this.authenticator.addServerInfoByString(response.getWwwAuthenticate());
        this.authenticator.addClientInfo(this.uri.toString(), ERtspMethod.DESCRIBE.getCode());
        request = new RtspDescribeRequest(this.uri, Collections.singletonList(ERtspAcceptContent.SDP), this.authenticator);
        response = (RtspDescribeResponse) this.readFromServer(request);
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.DESCRIBE, response.getStatusCode().getCode()));
        }
        if (response.getSdp().getSession() == null) {
            throw new RtspCommException(String.format("RTSP[%s]没有Session", ERtspMethod.DESCRIBE));
        }
        if (!response.getSdp().getMedias().isEmpty()) {
            throw new RtspCommException(String.format("RTSP[%s]没有Media", ERtspMethod.DESCRIBE));
        }
        this.sdp = response.getSdp();
    }

    protected void setup() {
        if (!methods.contains(ERtspMethod.SETUP)) {
            throw new RtspCommException("RTSP不支持[SETUP]");
        }
        RtspSdpMediaAttrControl control = this.sdp.getMedias().get(0).getAttributeControl();
        URI actualUri = URI.create(control.getUri());
        RtspTransport reqTransport = new RtspTransport();
        reqTransport.setProtocol(this.sdp.getMedias().get(0).getMediaDesc().getProtocol());
        reqTransport.setCastMode("unicast");
        reqTransport.setRtpClientPort(60802);
        reqTransport.setRtcpClientPort(60803);
        if (this.needAuthorization && this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", ERtspMethod.SETUP));
        }
        RtspSetupRequest request = this.needAuthorization ? new RtspSetupRequest(actualUri, reqTransport, this.authenticator)
                : new RtspSetupRequest(actualUri, reqTransport);
        RtspSetupResponse response = (RtspSetupResponse) this.readFromServer(request);
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.SETUP, response.getStatusCode().getCode()));
        }
        this.transport = response.getTransport();
        this.session = response.getSession();
    }

    protected void play() {
        if (!methods.contains(ERtspMethod.PLAY)) {
            throw new RtspCommException("RTSP不支持[SETUP]");
        }
        if (this.needAuthorization && this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", ERtspMethod.PLAY));
        }
        RtspPlayRequest request = this.needAuthorization ? new RtspPlayRequest(this.uri, this.session)
                : new RtspPlayRequest(this.uri, this.session, this.authenticator);
        RtspPlayResponse response = (RtspPlayResponse) this.readFromServer(request);
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.PLAY, response.getStatusCode().getCode()));
        }
        this.rtpInfos = response.getRtpInfo();
    }

    protected void teardown() {
        if (!methods.contains(ERtspMethod.TEARDOWN)) {
            throw new RtspCommException("RTSP不支持[TEARDOWN]");
        }
        if (this.needAuthorization && this.authenticator == null) {
            throw new RtspCommException(String.format("RTSP[%s]交互中authenticator为null", ERtspMethod.TEARDOWN));
        }
        RtspTeardownRequest request = this.needAuthorization ? new RtspTeardownRequest(this.uri, this.session)
                : new RtspTeardownRequest(this.uri, this.session, this.authenticator);
        RtspTeardownResponse response = (RtspTeardownResponse) this.readFromServer(request);
        if (response.getStatusCode() != ERtspStatusCode.OK) {
            throw new RtspCommException(String.format("RTSP[%s]交互返回状态为[%s]", ERtspMethod.TEARDOWN, response.getStatusCode().getCode()));
        }
    }
}
