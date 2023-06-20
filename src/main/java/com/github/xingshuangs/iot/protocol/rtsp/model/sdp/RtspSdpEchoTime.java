package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import lombok.Data;

/**
 * 重复时间
 *
 * @author xingshuang
 */
@Data
public class RtspSdpEchoTime {

    /**
     * 重复间隔
     */
    private Integer echoInterval;

    /**
     * 持续时长
     */
    private Integer duringTime;

    /**
     * 相对起始时间的偏移链表
     */
    private Integer offset;

}
