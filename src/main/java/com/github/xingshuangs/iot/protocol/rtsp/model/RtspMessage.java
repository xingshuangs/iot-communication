package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectString;
import com.github.xingshuangs.iot.utils.SequenceNumberUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.*;

/**
 * RTSP基础消息
 *
 * @author xingshuang
 */
@Getter
public class RtspMessage implements IObjectString {

    public static final String VERSION_1_0 = "RTSP/1.0";

    public RtspMessage() {
        this(VERSION_1_0, new HashMap<>());
    }

    public RtspMessage(String version, Map<String, String> headers) {
        this.cSeq = SequenceNumberUtil.getUint16Number();
        this.version = version;
        this.headers = headers;
    }

    /**
     * 序列号
     */
    protected int cSeq;

    /**
     * 版本号
     */
    protected String version;

    /**
     * 请求头
     */
    protected Map<String, String> headers;

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VERSION_1_0).append(CRLF)
                .append(C_SEQ).append(COLON).append(this.cSeq);
        return sb.toString();
    }
}
