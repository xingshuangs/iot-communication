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

    /**
     * 通道
     */
    private RtspTransport transport;

    /**
     * 特殊的会话信息
     */
    private RtspSessionInfo sessionInfo;

    public static RtspSetupResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetupResponse时字符串为空");
        }
        RtspSetupResponse response = new RtspSetupResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        if (map.containsKey(SESSION)) {
            response.sessionInfo = RtspSessionInfo.fromString(map.get(SESSION).trim());
            response.session = response.sessionInfo.getSessionId();
        }
        if (map.containsKey(TRANSPORT)) {
            response.transport = RtspTransport.fromString(map.get(TRANSPORT).trim());
        }
        return response;
    }
}
