package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspGetParameterResponse extends RtspMessageResponse {

    private final Map<String, String> parameters = new LinkedHashMap<>();

    public static RtspGetParameterResponse fromHeaderString(final String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspGetParameterResponse时字符串为空");
        }
        RtspGetParameterResponse response = new RtspGetParameterResponse();
        response.parseHeaderAndReturnMap(src);
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
