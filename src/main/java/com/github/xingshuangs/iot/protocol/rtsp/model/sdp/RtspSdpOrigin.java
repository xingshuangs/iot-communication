package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 源
 * - 1517245007527432 1517245007527432 IN IP4 10.3.8.202
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

    public static RtspSdpOrigin fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析Origin部分数据源错误");
        }

        RtspSdpOrigin origin = new RtspSdpOrigin();
        String[] split = src.split(SP);
        if (split.length != 6) {
            throw new RtspCommException("RtspSdpOrigin数据有误，无法解析");
        }
        origin.username = split[0];
        origin.sessionId = split[1];
        origin.sessionVersion = split[2];
        origin.networkType = split[3];
        origin.addressType = split[4];
        origin.unicastAddress = split[5];
        return origin;
    }
}
