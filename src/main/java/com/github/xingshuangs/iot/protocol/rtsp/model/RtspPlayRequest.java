package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COLON;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.RANGE;

/**
 * Play请求
 *
 * @author xingshuang
 */
@Getter
public class RtspPlayRequest extends RtspMessageRequest {

    private String range = "";

    public RtspPlayRequest(URI uri, int session) {
        this(uri, session, null, null);
    }

    public RtspPlayRequest(URI uri, int session, String range) {
        this(uri, session, range, null);
    }

    public RtspPlayRequest(URI uri, int session, String range, AbstractAuthenticator authenticator) {
        super(ERtspMethod.PLAY, uri, session, authenticator);
        this.range = range;
    }

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toObjectString());

        if (this.range != null && !this.range.equals("")) {
            sb.append(RANGE).append(COLON).append(this.range).append(CRLF);
        }
        return sb.toString();
    }
}
