package com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;

/**
 * 附加信息：控制部分
 *
 * @author xingshuang
 */
public class RtspSdpMediaAttrControl {

    /**
     * 地址
     */
    private String uri;

    /**
     * 轨道ID
     */
    private Integer trackID;

    public static RtspSdpMediaAttrControl fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrControl部分数据源错误");
        }
        RtspSdpMediaAttrControl control = new RtspSdpMediaAttrControl();
        int i = src.indexOf(EQUAL);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrControl数据有误，无法解析");
        }
        control.uri = src;
        control.trackID = Integer.parseInt(src.substring(i + 1));
        return control;
    }
}
