package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话
 *
 * @author xingshuang
 */
@Data
public class RtspSdpSession {

    /**
     * 版本（必选）
     */
    private Integer version = 0;

    /**
     * 源（必选）
     */
    private RtspSdpOrigin origin;

    /**
     * 会话名称（必选）
     */
    private String sessionName;

    /**
     * 时间（必选）
     */
    private RtspSdpTime time;

    /**
     * 会话信息(可选)
     */
    private String sessionInfo;

    /**
     * 统一资源描述(可选)
     */
    private String uri;

    /**
     * 电子邮件(可选)
     */
    private String email;

    /**
     * 电话号码(可选)
     */
    private String phone;

    /**
     * 连接信息(可选)
     */
    private RtspSdpConnection connection;

    /**
     * 带宽(可选)
     */
    private RtspSdpBandwidth bandwidth;

    /**
     * 重复时间(可选)
     */
    private RtspSdpEchoTime echoTime;

    /**
     * 附加信息(可选)
     */
    private Map<String,String> attributes = new HashMap<>();
}
