package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspSetParameterResponse extends RtspMessageResponse {

    public static RtspSetParameterResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetParameterResponse时字符串为空");
        }
        RtspSetParameterResponse response = new RtspSetParameterResponse();
        Map<String, String> map = response.parseDataAndReturnMap(src);
        // TODO: 还有body
        return response;
    }

}
