package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

/**
 * 带宽
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
}
