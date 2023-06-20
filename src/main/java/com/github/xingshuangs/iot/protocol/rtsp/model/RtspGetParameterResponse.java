package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspSessionInfo;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SESSION;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspGetParameterResponse extends RtspMessageResponse {

    private final Map<String, String> parameters = new LinkedHashMap<>();

    /**
     * 特殊的会话信息
     */
    private RtspSessionInfo sessionInfo;

    public static RtspGetParameterResponse fromHeaderString(final String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspGetParameterResponse时字符串为空");
        }
        RtspGetParameterResponse response = new RtspGetParameterResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);

        // 会话ID
        if (map.containsKey(SESSION)) {
            response.sessionInfo = RtspSessionInfo.fromString(map.get(SESSION).trim());
            response.session = response.sessionInfo.getSessionId();
        }
        return response;
    }

    /**
     * 通过字符串添加body内容
     *
     * @param src 字符串
     */
    @Override
    public void addBodyFromString(String src) {
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, CRLF, COLON);
        this.getParameters().putAll(map);
    }
}
