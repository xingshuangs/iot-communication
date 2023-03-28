package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import lombok.Data;

import java.util.Map;


/**
 * RTSP消息请求
 *
 * @author xingshuang
 */
@Data
public class RtspMessageResponse extends RtspMessage {
    /**
     * 状态码
     */
    private ERtspStatusCode statusCode = ERtspStatusCode.OK;

    public RtspMessageResponse() {
    }

    public RtspMessageResponse(ERtspStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public RtspMessageResponse(String version, Map<String, String> headers, ERtspStatusCode statusCode) {
        super(version, headers);
        this.statusCode = statusCode;
    }

    @Override
    public String toObjectString() {
        return super.toObjectString();
    }
}
