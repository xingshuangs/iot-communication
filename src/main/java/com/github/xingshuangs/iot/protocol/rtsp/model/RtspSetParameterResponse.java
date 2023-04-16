package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.utils.StringSplitUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspSetParameterResponse extends RtspMessageResponse {

    private final List<String> parameterNames = new ArrayList<>();

    public static RtspSetParameterResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetParameterResponse时字符串为空");
        }
        RtspSetParameterResponse response = new RtspSetParameterResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);

        String body = response.parseMessageBody(src);
        List<String> list = StringSplitUtil.splitOneStepByLine(body, CRLF);
        response.parameterNames.addAll(list);
        return response;
    }
}
