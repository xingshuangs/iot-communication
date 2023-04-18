package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 媒体描述
 * video 0 RTP/AVP 96
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaDesc {
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

    public static RtspSdpMediaDesc fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaDesc部分数据源错误");
        }
        RtspSdpMediaDesc desc = new RtspSdpMediaDesc();
        String[] split = src.split(SP);
        if (split.length != 4) {
            throw new RtspCommException("RtspSdpMediaDesc数据有误，无法解析");
        }
        desc.type = split[0];
        desc.port = Integer.parseInt(split[1]);
        desc.protocol = split[2];
        desc.payloadFormatNumber = Integer.parseInt(split[3]);
        return desc;
    }
}
