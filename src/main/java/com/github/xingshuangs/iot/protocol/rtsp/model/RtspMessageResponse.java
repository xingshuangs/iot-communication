package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspKey.SP;


/**
 * RTSP消息请求
 *
 * @author xingshuang
 */
@Getter
public class RtspMessageResponse extends RtspMessage {
    /**
     * 状态码
     */
    protected ERtspStatusCode statusCode = ERtspStatusCode.OK;

    public RtspMessageResponse() {
    }

    public RtspMessageResponse(ERtspStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public RtspMessageResponse(String version, Map<String, String> headers, ERtspStatusCode statusCode) {
        super(version, headers);
        this.statusCode = statusCode;
    }

    public void assignVersionAndStatusCode(String src) {
        String[] versionContent = src.split(SP);
        this.version = versionContent[0];
        this.statusCode = ERtspStatusCode.from(Integer.parseInt(versionContent[1]));
    }

    /**
     * 将数据解析成Map
     *
     * @param data 字符串数组
     * @return Map数据类型
     */
    public Map<String, String> getMapByData(String[] data) {
        Map<String, String> res = new HashMap<>();
        for (String item : data) {
            String[] split = item.split(COLON);
            if (split.length == 2) {
                res.put(split[0].trim(), split[1].trim());
            }
        }
        return res;
    }
}
