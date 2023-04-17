package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import lombok.Data;

import java.util.List;

/**
 * 会话描述协议
 *
 * @author xingshuang
 */
@Data
public class RtspSdp {

    /**
     * 会话描述
     */
    private RtspSdpSession session;

    /**
     * 媒体描述
     */
    private List<RtspSdpMedia> medias;
}
