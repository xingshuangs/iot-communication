package com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SEMICOLON;

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
        int start = src.indexOf(EQUAL);
        int end = src.indexOf(SEMICOLON);
        if (start == -1) {
            throw new RtspCommException("RtspSdpMediaAttrHeader数据有误，无法解析");
        }
        if (end == -1) {
            header.mediaInfo = src.substring(start + 1);
        } else {
            header.mediaInfo = src.substring(start + 1, end);
        }
        return header;
    }
}
