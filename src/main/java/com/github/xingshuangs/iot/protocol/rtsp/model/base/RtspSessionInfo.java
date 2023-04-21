package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import lombok.Data;

/**
 * RTSP的会话部分
 * 814632351;timeout=60
 *
 * @author xingshuang
 */
@Data
public class RtspSessionInfo {

    /**
     * 会话Id
     */
    private String sessionId;

    /**
     * 毫秒级的超时时间，实际是秒级
     */
    private int timeout;

    public static RtspSessionInfo fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspSessionInfo部分数据解析错误");
        }
        RtspSessionInfo sessionInfo = new RtspSessionInfo();
        int commaIndex = src.indexOf(";");
        if (commaIndex != -1) {
            sessionInfo.sessionId = src.substring(0, commaIndex);
        } else {
            sessionInfo.sessionId = src;
        }

        int equalIndex = src.indexOf("=");
        if (equalIndex != -1) {
            sessionInfo.timeout = Integer.parseInt(src.substring(equalIndex + 1)) * 1000;
        }
        return sessionInfo;
    }
}
