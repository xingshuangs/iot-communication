package com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;

/**
 * 头
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrHeader {

    private String mediaInfo;

    public static RtspSdpMediaAttrHeader fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrHeader部分数据源错误");
        }
        RtspSdpMediaAttrHeader header = new RtspSdpMediaAttrHeader();
        int i = src.indexOf(EQUAL);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrHeader数据有误，无法解析");
        }
        header.mediaInfo = src.substring(i + 1);
        return header;
    }
}
