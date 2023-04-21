package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;

/**
 * RTSP响应构建器
 *
 * @author xingshuang
 */
public class RtspMessageResponseBuilder {

    private RtspMessageResponseBuilder() {
    }

    public static RtspMessageResponse fromString(String src, RtspMessageRequest request) {

        switch (request.getMethod()) {
            case OPTIONS:
                return RtspOptionResponse.fromHeaderString(src);
            case DESCRIBE:
                return RtspDescribeResponse.fromHeaderString(src);
            case SETUP:
                return RtspSetupResponse.fromHeaderString(src);
            case PLAY:
                return RtspPlayResponse.fromHeaderString(src);
            case TEARDOWN:
                return RtspTeardownResponse.fromHeaderString(src);
            default:
                throw new RtspCommException(String.format("未实现对该Method[%s]的数据解析", request.getMethod().getCode()));
        }
    }
}
