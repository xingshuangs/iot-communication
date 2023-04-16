package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Describe响应
 *
 * @author xingshuang
 */
@Getter
public class RtspDescribeResponse extends RtspMessageResponse {

    private String wwwAuthenticate = "";

    public static RtspDescribeResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspDescribeResponse时字符串为空");
        }
        String[] srcSplit = src.split(CRLF);
        if (srcSplit.length < 2) {
            throw new RtspCommException("解析RtspOptionResponse返回格式有误");
        }
        RtspDescribeResponse response = new RtspDescribeResponse();
        // 解析版本和状态码
        response.assignVersionAndStatusCode(srcSplit[0]);
        Map<String, String> map = response.getMapByData(srcSplit);
        // 解析序列号
        response.cSeq = map.containsKey(C_SEQ) ? Integer.parseInt(map.get(C_SEQ).trim()) : 0;
        response.session = map.containsKey(SESSION) ? Integer.parseInt(map.get(SESSION).trim()) : -1;

        // TODO: 待实现

        return response;
    }

}
