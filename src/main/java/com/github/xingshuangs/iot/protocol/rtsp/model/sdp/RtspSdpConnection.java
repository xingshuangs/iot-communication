package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 连接信息
 * IN IP4 0.0.0.0
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

    public static RtspSdpConnection fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析Connection部分数据源错误");
        }

        RtspSdpConnection connection = new RtspSdpConnection();
        String[] split = src.split(SP);
        if (split.length != 3) {
            throw new RtspCommException("RtspSdpConnection数据有误，无法解析");
        }
        connection.networkType = split[0];
        connection.addressType = split[1];
        connection.connectionAddress = split[2];
        return connection;
    }
}
