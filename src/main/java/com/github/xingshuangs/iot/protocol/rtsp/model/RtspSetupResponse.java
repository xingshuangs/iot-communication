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
public class RtspSetupResponse extends RtspMessageResponse {

    private RtspTransport transport;

    public static RtspSetupResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetupResponse时字符串为空");
        }
        RtspSetupResponse response = new RtspSetupResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        if (map.containsKey(TRANSPORT)) {
            response.transport = RtspTransport.extractFrom(map.get(TRANSPORT));
        }
        return response;
    }
}
