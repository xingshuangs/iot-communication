package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspAcceptContent;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.ACCEPT;

/**
 * Describe请求
 *
 * @author xingshuang
 */
@Getter
public class RtspDescribeRequest extends RtspMessageRequest {

    private List<ERtspAcceptContent> acceptContents = new ArrayList<>();

    public RtspDescribeRequest(URI uri) {
        super(ERtspMethod.DESCRIBE, uri);
    }

    public RtspDescribeRequest(URI uri, List<ERtspAcceptContent> acceptContents) {
        super(ERtspMethod.DESCRIBE, uri);
        this.acceptContents = acceptContents;
    }

    public RtspDescribeRequest(URI uri, List<ERtspAcceptContent> acceptContents, AbstractAuthenticator authenticator) {
        super(ERtspMethod.DESCRIBE, uri, authenticator);
        this.acceptContents = acceptContents;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (!this.acceptContents.isEmpty()) {
            sb.append(ACCEPT)
                    .append(COLON + SP)
                    .append(this.acceptContents.stream().map(ERtspAcceptContent::getCode).collect(Collectors.joining(COMMA)))
                    .append(CRLF);
        }
    }
}
