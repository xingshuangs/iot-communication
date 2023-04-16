package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.RTP_INFO;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspPlayResponse extends RtspMessageResponse {
    private String rtpInfo = "";

    public static RtspPlayResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspPlayResponse时字符串为空");
        }
        RtspPlayResponse response = new RtspPlayResponse();
        Map<String, String> map = response.parseDataAndReturnMap(src);
        response.rtpInfo = map.containsKey(RTP_INFO) ? map.get(RTP_INFO).trim() : "";
        return response;
    }

}
