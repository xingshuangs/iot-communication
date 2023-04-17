package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

/**
 * 连接信息
 *
 * @author xingshuang
 */
@Data
public class RtspSdpConnection {
    /**
     * 网络类型
     */
    private String networkType;

    /**
     * 地址类型
     */
    private String addressType;

    /**
     * 连接地址
     */
    private String connectionAddress;
}
