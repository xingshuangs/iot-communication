package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

/**
 * 时间
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
}
