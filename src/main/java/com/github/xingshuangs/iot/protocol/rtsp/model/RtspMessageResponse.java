package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;


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

    private void extractVersionAndStatusCode(String src) {
        String[] versionContent = src.split(SP);
        this.version = versionContent[0].trim();
        this.statusCode = ERtspStatusCode.from(Integer.parseInt(versionContent[1].trim()));
    }

    private void extractCommonData(Map<String, String> map) {
        // 解析序列号
        this.cSeq = map.containsKey(C_SEQ) ? Integer.parseInt(map.get(C_SEQ).trim()) : 0;
        this.session = map.containsKey(SESSION) ? Integer.parseInt(map.get(SESSION).trim()) : -1;
    }

    public Map<String, String> parseDataAndReturnMap(String src) {
        int i = src.indexOf(CRLF);
        this.extractVersionAndStatusCode(src.substring(0, i));
        Map<String, String> map = this.getMapByData(src.substring(i));
        this.extractCommonData(map);
        return map;
    }

    /**
     * 将数据解析成Map
     *
     * @param data 字符串数组
     * @return Map数据类型
     */
    private Map<String, String> getMapByData(String src) {
        String[] data = src.split(CRLF);
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
