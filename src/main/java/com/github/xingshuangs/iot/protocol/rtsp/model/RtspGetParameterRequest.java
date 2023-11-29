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
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspContentType;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_LENGTH;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspEntityHeaderFields.CONTENT_TYPE;

/**
 * GetParameter请求
 *
 * @author xingshuang
 */
@Getter
public final class RtspGetParameterRequest extends RtspMessageRequest {

    /**
     * 有序set集合，参数名
     */
    private final Set<String> parameterNames = new LinkedHashSet<>();

    public RtspGetParameterRequest(URI uri, String session) {
        super(ERtspMethod.GET_PARAMETER, uri, session);
    }

    public RtspGetParameterRequest(URI uri, String session, AbstractAuthenticator authenticator) {
        super(ERtspMethod.GET_PARAMETER, uri, session, authenticator);
    }

    public void addParameter(String name) {
        this.parameterNames.add(name);
    }

    public void removeParameter(String name) {
        this.parameterNames.remove(name);
    }

    public Set<String> getParameters() {
        return Collections.unmodifiableSet(this.parameterNames);
    }

    @Override
    protected void addEntityHeader(StringBuilder sb) {
        if (!this.parameterNames.isEmpty()) {
            String names = String.join(CRLF, this.parameterNames) + CRLF;
            sb.append(CONTENT_TYPE).append(COLON + SP).append(ERtspContentType.PARAMETER.getCode()).append(CRLF);
            sb.append(CONTENT_LENGTH).append(COLON + SP).append(names.length()).append(CRLF);
        }
    }

    @Override
    protected void addMessageBody(StringBuilder sb) {
        if (!this.parameterNames.isEmpty()) {
            String names = String.join(CRLF, this.parameterNames) + CRLF;
            sb.append(names);
        }
    }
}
