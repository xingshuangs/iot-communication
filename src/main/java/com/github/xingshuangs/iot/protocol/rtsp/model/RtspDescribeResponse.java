package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.RtspSdp;
import lombok.Getter;

/**
 * Describe响应
 *
 * @author xingshuang
 */
@Getter
public final class RtspDescribeResponse extends RtspMessageResponse {

    /**
     * SDP描述部分
     */
    private RtspSdp sdp;

    public static RtspDescribeResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspDescribeResponse时字符串为空");
        }
        RtspDescribeResponse response = new RtspDescribeResponse();
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
        this.sdp = RtspSdp.fromString(src);
    }
}
