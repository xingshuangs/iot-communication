package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectString;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.*;

/**
 * RTSP基础消息
 *
 * @author xingshuang
 */
@Getter
public class RtspMessage implements IObjectString {

    public static final String VERSION_1_0 = "RTSP/1.0";

    private static final AtomicInteger index = new AtomicInteger();

    public RtspMessage() {
        this(VERSION_1_0, new HashMap<>());
    }

    public RtspMessage(String version, Map<String, String> headers) {
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

    /**
     * 获取2字节大小的最新序号，0-65536
     *
     * @return 序号
     */
    public static int getUint16Number() {
        return getNumber(65536);
    }

    /**
     * 自定义最大值的最新序号
     *
     * @return 序号
     */
    public static int getNumber(int max) {
        int res = index.getAndIncrement();
        if (res >= max) {
            index.set(0);
            res = 0;
        }
        return res;
    }
}
