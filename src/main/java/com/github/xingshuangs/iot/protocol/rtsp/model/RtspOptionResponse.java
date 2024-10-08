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


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.PUBLIC;

/**
 * Option response
 *
 * @author xingshuang
 */
@Getter
public final class RtspOptionResponse extends RtspMessageResponse {

    /**
     * Public methods
     * (可用的方法)
     */
    private List<ERtspMethod> publicMethods = new ArrayList<>();

    public static RtspOptionResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("src is null or empty in RtspOptionResponse");
        }
        RtspOptionResponse response = new RtspOptionResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        // 解析公有方法
        if (map.containsKey(PUBLIC)) {
            String publicStr = map.get(PUBLIC).trim();
            response.publicMethods = Stream.of(publicStr.split(COMMA))
                    .map(x -> ERtspMethod.from(x.trim()))
                    .collect(Collectors.toList());
        }
        return response;
    }

    @Override
    protected void addResponseHeader(StringBuilder sb) {
        if (!this.publicMethods.isEmpty()) {
            String str = this.publicMethods.stream().map(ERtspMethod::getCode).collect(Collectors.joining(COMMA));
            sb.append(PUBLIC).append(COLON + SP).append(str).append(CRLF);
        }
    }
}
