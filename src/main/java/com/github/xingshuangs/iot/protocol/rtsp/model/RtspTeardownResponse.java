package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Getter;

/**
 * Teardown响应
 *
 * @author xingshuang
 */
@Getter
public class RtspTeardownResponse extends RtspMessageResponse {

    public static RtspTeardownResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspSetupResponse时字符串为空");
        }
        RtspTeardownResponse response = new RtspTeardownResponse();
        response.parseHeaderAndReturnMap(src);
        return response;
    }

}
