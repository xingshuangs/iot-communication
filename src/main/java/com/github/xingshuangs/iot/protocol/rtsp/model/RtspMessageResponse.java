package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspContentType;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspStatusCode;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.AUTHORIZATION;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.USER_AGENT;


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

    /**
     * 内容类型
     */
    protected ERtspContentType contentType;

    /**
     * 内容基础
     */
    protected String contentBase;

    /**
     * 内容长度
     */
    protected Integer contentLength = -1;

    /**
     * 缓存控制
     */
    protected String cacheControl;

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
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(headerStr, CRLF, COLON);
        this.extractResponseHeader(map);
        this.extractBodyHeader(map);
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
    private void extractResponseHeader(Map<String, String> map) {
        // 解析序列号，可能有两种不同的seq
        if (map.containsKey(C_SEQ)) {
            this.cSeq = Integer.parseInt(map.get(C_SEQ).trim());
        }
        if (map.containsKey(C_SEQ1)) {
            this.cSeq = Integer.parseInt(map.get(C_SEQ1).trim());
        }
    }

    /**
     * 提取body的头
     *
     * @param map map
     */
    private void extractBodyHeader(Map<String, String> map) {
        this.contentType = map.containsKey(CONTENT_TYPE) ? ERtspContentType.from(map.get(CONTENT_TYPE)) : null;
        this.contentBase = map.getOrDefault(CONTENT_BASE, "");
        this.contentLength = map.containsKey(CONTENT_LENGTH) ? Integer.parseInt(map.get(CONTENT_LENGTH)) : null;
        this.cacheControl = map.getOrDefault(CACHE_CONTROL, "");
    }

    public void addBodyFromString(String src) {
        // NOOP
    }

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        this.addResponseLine(sb);
        this.addGeneralHeader(sb);
        this.addCommonResponseHeader(sb);
        this.addResponseHeader(sb);
        this.addCommonEntityHeader(sb);
        this.addEntityHeader(sb);
        sb.append(CRLF);
        this.addMessageBody(sb);
        return sb.toString();
    }

    private void addResponseLine(StringBuilder sb) {
        // Status-Line = RTSP-Version SP Status-Code SP Reason-Phrase CRLF
        sb.append(this.version).append(SP).append(this.statusCode.getCode()).append(SP).append(this.statusCode.getDescription()).append(CRLF);
    }

    private void addGeneralHeader(StringBuilder sb) {
        // CSeq: 1
        sb.append(C_SEQ).append(COLON + SP).append(this.cSeq).append(CRLF);
    }

    private void addCommonResponseHeader(StringBuilder sb) {
        // session
        if (this.session != null && !this.session.equals("")) {
            sb.append(SESSION).append(COLON + SP).append(this.session).append(CRLF);
        }
    }

    private void addCommonEntityHeader(StringBuilder sb) {
        if (this.contentType != null) {
            sb.append(CONTENT_TYPE).append(COLON + SP).append(this.contentType.getCode()).append(CRLF);
        }
        if (this.contentBase != null && !this.contentBase.equals("")) {
            sb.append(CONTENT_BASE).append(COLON + SP).append(this.contentBase).append(CRLF);
        }
        if (this.contentLength != null && this.contentLength >= 0) {
            sb.append(CONTENT_LENGTH).append(COLON + SP).append(this.contentLength).append(CRLF);
        }
        if (this.cacheControl != null && !this.cacheControl.equals("")) {
            sb.append(CACHE_CONTROL).append(COLON + SP).append(this.cacheControl).append(CRLF);
        }
    }

    protected void addResponseHeader(StringBuilder sb) {
        // NOOP
    }

    protected void addEntityHeader(StringBuilder sb) {
        // NOOP
    }

    protected void addMessageBody(StringBuilder sb) {
        // NOOP
    }
}
