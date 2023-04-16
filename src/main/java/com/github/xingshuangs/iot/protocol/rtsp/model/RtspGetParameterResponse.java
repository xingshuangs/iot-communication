package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

import java.util.Map;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspGetParameterResponse extends RtspMessageResponse {

    public static RtspGetParameterResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspGetParameterResponse时字符串为空");
        }
        RtspGetParameterResponse response = new RtspGetParameterResponse();
        Map<String, String> map = response.parseDataAndReturnMap(src);

        return response;
    }

}
