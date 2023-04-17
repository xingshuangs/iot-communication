package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

/**
 * 源
 *
 * @author xingshuang
 */
@Data
public class RtspSdpOrigin {
    /**
     * 会话ID
     */
    private String username;

    /**
     *
     */
    private String sessionId;

    /**
     * 会话版本
     */
    private String sessionVersion;

    /**
     * 网络类型
     */
    private String networkType;

    /**
     * 地址类型
     */
    private String addressType;

    /**
     * 单播地址
     */
    private String unicastAddress;
}
