package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;

/**
 * 带宽
 * AS:5050
 *
 * @author xingshuang
 */
@Data
public class RtspSdpBandwidth {

    /**
     * 带宽类型
     * i）CT（Conference Total）表示多会话广播中会话或者媒体使用的最大带宽建议值，CT值相当于所有会话带宽值。
     * ii）AS(Application-Specific)是指具体某个应用程序所占用的总带宽建议值，相当于最大应用程序带宽值，它仅值单媒体在单点所占用的带宽。
     */
    private String type;

    /**
     * 带宽值
     */
    private Integer value;

    public static RtspSdpBandwidth fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析Bandwidth部分数据源错误");
        }
        RtspSdpBandwidth bandwidth = new RtspSdpBandwidth();
        String[] split = src.split(COLON);
        if (split.length != 2) {
            throw new RtspCommException("RtspSdpBandwidth数据有误，无法解析");
        }
        bandwidth.type = split[0];
        bandwidth.value = Integer.parseInt(split[1]);
        return bandwidth;
    }
}
