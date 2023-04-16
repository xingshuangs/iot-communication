package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.utils.StringSplitUtil;
import lombok.Getter;

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

    /**
     * 解析数据并返回Map
     *
     * @param src 字符串数据
     * @return Map
     */
    public Map<String, String> parseHeaderAndReturnMap(String src) {
        int startIndex = src.indexOf(CRLF);
        int endIndex = src.indexOf(CRLF + CRLF);
        this.extractVersionAndStatusCode(src.substring(0, startIndex));
        String headerStr = src.substring(startIndex, endIndex + 4);
        Map<String, String> map = StringSplitUtil.splitTwoStepByLine(headerStr, CRLF, COLON);
        this.extractCommonData(map);
        return map;
    }

    /**
     * 提取版本和状态码
     *
     * @param src 字符串
     */
    private void extractVersionAndStatusCode(String src) {
        String[] versionContent = src.split(SP);
        // 版本
        this.version = versionContent[0].trim();
        // 状态码
        this.statusCode = ERtspStatusCode.from(Integer.parseInt(versionContent[1].trim()));
    }

    /**
     * 提取公共数据
     *
     * @param map map
     */
    private void extractCommonData(Map<String, String> map) {
        // 解析序列号
        this.cSeq = map.containsKey(C_SEQ) ? Integer.parseInt(map.get(C_SEQ).trim()) : 0;
        // 会话ID
        this.session = map.containsKey(SESSION) ? Integer.parseInt(map.get(SESSION).trim()) : -1;
    }

    /**
     * 提取消息内容
     *
     * @param src 字符串
     * @return 内容
     */
    public String parseMessageBody(String src) {
        int endIndex = src.indexOf(CRLF + CRLF);
        return src.substring(endIndex + 4);
    }
}
