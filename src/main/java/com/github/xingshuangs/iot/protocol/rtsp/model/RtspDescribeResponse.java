package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdp;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.WWW_AUTHENTICATE;

/**
 * Describe响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspDescribeResponse extends RtspMessageResponse {

    /**
     * WWW-Authenticate: Digest realm="IP Camera(D2959)", nonce="c9f3698bf99b5f0a77f3960d35df7776", stale="FALSE"\r\n
     */
    private String wwwAuthenticate = "";

    /**
     * SDP描述部分
     */
    private RtspSdp sdp;

    public static RtspDescribeResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspDescribeResponse时字符串为空");
        }
        RtspDescribeResponse response = new RtspDescribeResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);

        if (response.statusCode == ERtspStatusCode.UNAUTHORIZED) {
            response.wwwAuthenticate = map.getOrDefault(WWW_AUTHENTICATE, "").trim();
            return response;
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
        this.sdp = RtspSdp.fromString(src);
    }
}
