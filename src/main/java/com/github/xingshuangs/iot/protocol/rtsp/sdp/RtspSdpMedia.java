package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

import java.util.Map;

/**
 * 媒体描述
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMedia {

    /**
     * 媒体类型（必选）
     */
    private String type;

    /**
     * 端口（必选）
     */
    private Integer port;

    /**
     * 协议（必选）
     */
    private String protocol;

    /**
     * 格式类型（必选）
     */
    private Integer payloadFormatNumber;

    /**
     * 连接信息(可选)
     */
    private RtspSdpConnection connection;

    /**
     * 带宽(可选)
     */
    private RtspSdpBandwidth bandwidth;

    /**
     * 加密秘钥(可选)
     */
    private RtspSdpEncryptKey encryptKey;

    /**
     * 附加信息(可选)
     */
    private Map<String,String> attributes;

}
