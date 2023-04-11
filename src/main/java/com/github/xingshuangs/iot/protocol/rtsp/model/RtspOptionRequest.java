package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;

import java.net.URI;

/**
 * Option请求
 *
 * @author xingshuang
 */
public class RtspOptionRequest extends RtspMessageRequest{

    public RtspOptionRequest(URI uri) {
        super(ERtspMethod.OPTIONS, uri);
    }
}
