package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 时间
 * 0 0
 *
 * @author xingshuang
 */
@Data
public class RtspSdpTime {

    /**
     * 起始时间
     */
    private Integer startTime;

    /**
     * 结束时间
     */
    private Integer endTime;

    public static RtspSdpTime fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析Time部分数据源错误");
        }
        RtspSdpTime time = new RtspSdpTime();
        String[] split = src.split(SP);
        if (split.length != 2) {
            throw new RtspCommException("RtspSdpTime数据有误，无法解析");
        }
        time.startTime = Integer.parseInt(split[0]);
        time.endTime = Integer.parseInt(split[1]);
        return time;
    }
}
