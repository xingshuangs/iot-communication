package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SEMICOLON;

/**
 * RTP的信息
 *
 * @author xingshuang
 */
@Data
public class RtspRtpInfo {

    /**
     * 地址
     */
    private String url;

    /**
     * 序列号
     */
    private Long seq;

    /**
     * RTP时间
     */
    private Long rtpTime;

    public static RtspRtpInfo fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspRtpInfo部分数据解析错误");
        }
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, SEMICOLON, EQUAL);
        RtspRtpInfo rtpInfo = new RtspRtpInfo();
        rtpInfo.url = map.getOrDefault("url", "");
        if (map.containsKey("seq")) {
            rtpInfo.seq = Long.parseLong(map.get("seq"));
        }
        if (map.containsKey("rtptime")) {
            rtpInfo.rtpTime = Long.parseLong(map.get("rtptime"));
        }
        return rtpInfo;
    }
}
