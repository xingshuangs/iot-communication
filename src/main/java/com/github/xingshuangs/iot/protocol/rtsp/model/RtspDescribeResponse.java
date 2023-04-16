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

    /**
     * WWW-Authenticate: Digest realm="IP Camera(D2959)", nonce="c9f3698bf99b5f0a77f3960d35df7776", stale="FALSE"\r\n
     */
    private String wwwAuthenticate = "";




    public static RtspDescribeResponse fromString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspDescribeResponse时字符串为空");
        }
        RtspDescribeResponse response = new RtspDescribeResponse();
        Map<String, String> map = response.parseDataAndReturnMap(src);
        // TODO: 待实现

        return response;
    }

}
