package com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 附加信息：媒体负载属性
 * rtpmap:96 H264/90000
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrRtpMap {

    /**
     * 负载编号
     */
    private Integer payloadNumber;

    /**
     * 负载格式
     */
    private String payloadFormat;

    /**
     * 时钟频率
     */
    private Integer clockFrequency;

    /**
     * 声道数量
     */
    private Integer soundTrackNumber;

    public static RtspSdpMediaAttrRtpMap fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrRtpMap部分数据源错误");
        }
        RtspSdpMediaAttrRtpMap rtpMap = new RtspSdpMediaAttrRtpMap();
        int i = src.indexOf(SP);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrRtpMap数据有误，无法解析");
        }
        rtpMap.payloadNumber = Integer.parseInt(src.substring(0, i));
        String sub = src.substring(i + 1).trim();
        String[] split = sub.split("/");
        if (split.length > 0) {
            rtpMap.payloadFormat = split[0];
        }
        if (split.length > 1) {
            rtpMap.clockFrequency = Integer.parseInt(split[1]);
        }
        if (split.length > 2) {
            rtpMap.soundTrackNumber = Integer.parseInt(split[2]);
        }
        return rtpMap;
    }
}
