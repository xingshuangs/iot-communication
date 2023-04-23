package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspSetParameterResponse extends RtspMessageResponse {

    private final List<String> parameterNames = new ArrayList<>();

    public static RtspSetParameterResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetParameterResponse时字符串为空");
        }
        RtspSetParameterResponse response = new RtspSetParameterResponse();
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
        List<String> list = StringSpUtil.splitOneStepByLine(src, CRLF);
        this.parameterNames.addAll(list);
    }
}
