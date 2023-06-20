package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CACHE_CONTROL;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.PUBLIC;

/**
 * Option响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspOptionResponse extends RtspMessageResponse {

    /**
     * 可用的方法
     */
    private List<ERtspMethod> publicMethods = new ArrayList<>();

    public static RtspOptionResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspOptionResponse时字符串为空");
        }
        RtspOptionResponse response = new RtspOptionResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        // 解析公有方法
        if (map.containsKey(PUBLIC)) {
            String publicStr = map.get(PUBLIC).trim();
            response.publicMethods = Stream.of(publicStr.split(COMMA))
                    .map(x -> ERtspMethod.from(x.trim()))
                    .collect(Collectors.toList());
        }
        return response;
    }

    @Override
    protected void addResponseHeader(StringBuilder sb) {
        if (!this.publicMethods.isEmpty()) {
            String str = this.publicMethods.stream().map(ERtspMethod::getCode).collect(Collectors.joining(COMMA));
            sb.append(PUBLIC).append(COLON + SP).append(str).append(CRLF);
        }
    }
}
