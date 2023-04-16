package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspPauseResponse extends RtspMessageResponse {

    public static RtspPauseResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspPlayResponse时字符串为空");
        }
        RtspPauseResponse response = new RtspPauseResponse();
        response.parseDataAndReturnMap(src);
        return response;
    }

}
