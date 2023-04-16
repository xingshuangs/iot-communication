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

/**
 * Option响应
 *
 * @author xingshuang
 */
@Getter
public class RtspOptionResponse extends RtspMessageResponse {

    /**
     * 可用的方法
     */
    private List<ERtspMethod> publicMethods = new ArrayList<>();

    public static RtspOptionResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspOptionResponse时字符串为空");
        }
        RtspOptionResponse response = new RtspOptionResponse();
        Map<String, String> map = response.parseDataAndReturnMap(src);
        // 解析公有方法
        if (map.containsKey(RtspResponseHeaderFields.PUBLIC)) {
            String publicStr = map.get(RtspResponseHeaderFields.PUBLIC).trim();
            response.publicMethods = Stream.of(publicStr.split(COMMA))
                    .map(x -> ERtspMethod.from(x.trim()))
                    .collect(Collectors.toList());
        }
        return response;
    }

}
