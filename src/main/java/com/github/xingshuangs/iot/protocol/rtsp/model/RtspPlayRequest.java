/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.protocol.rtsp.authentication.AbstractAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRange;
import lombok.Getter;

import java.net.URI;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspRequestHeaderFields.RANGE;

/**
 * Play request
 *
 * @author xingshuang
 */
@Getter
public final class RtspPlayRequest extends RtspMessageRequest {

    /**
     * Range of time.
     * (时间范围)
     */
    private final RtspRange range;

    public RtspPlayRequest(URI uri, String session) {
        this(uri, session, null, null);
    }

    public RtspPlayRequest(URI uri, String session, RtspRange range) {
        this(uri, session, range, null);
    }

    public RtspPlayRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        this(uri, session, null, authenticator);
    }

    public RtspPlayRequest(URI uri, String session, RtspRange range, AbstractAuthenticator authenticator) {
        super(ERtspMethod.PLAY, uri, session, authenticator);
        this.range = range;
    }

    @Override
    protected void addRequestHeader(StringBuilder sb) {
        if (this.range != null) {
            sb.append(RANGE).append(COLON + SP).append(this.range).append(CRLF);
        }
    }
}
