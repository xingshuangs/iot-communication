package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * Teardown响应
 *
 * @author xingshuang
 */
@Getter
public class RtspTeardownResponse extends RtspMessageResponse {

    /**
     * 会话编号
     */
    private int session = -1;

    public static RtspTeardownResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetupResponse时字符串为空");
        }
        String[] srcSplit = src.split(CRLF);
        if (srcSplit.length < 2) {
            throw new RtspCommException("解析RtspSetupResponse返回格式有误");
        }
        RtspTeardownResponse response = new RtspTeardownResponse();
        // 解析版本和状态码
        response.assignVersionAndStatusCode(srcSplit[0]);
        Map<String, String> map = response.getMapByData(srcSplit);
        // 解析序列号
        response.cSeq = map.containsKey(C_SEQ) ? Integer.parseInt(map.get(C_SEQ).trim()) : 0;
        response.session = map.containsKey(SESSION) ? Integer.parseInt(map.get(SESSION).trim()) : -1;
        return response;
    }

}
