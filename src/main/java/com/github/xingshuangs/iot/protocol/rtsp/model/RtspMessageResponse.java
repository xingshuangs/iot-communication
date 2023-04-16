package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;


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
        this.version = versionContent[0].trim();
        this.statusCode = ERtspStatusCode.from(Integer.parseInt(versionContent[1].trim()));
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
            int index = item.indexOf(COLON);
            if (index >= 0) {
                res.put(item.substring(0, index).trim(), item.substring(index + 1).trim());
            }
        }
        return res;
    }
}
