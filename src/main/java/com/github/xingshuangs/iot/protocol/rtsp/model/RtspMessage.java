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


import com.github.xingshuangs.iot.protocol.common.IObjectString;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * RTSP基础消息
 *
 * @author xingshuang
 */
@Getter
@Setter
public class RtspMessage implements IObjectString {

    public static final String VERSION_1_0 = "RTSP/1.0";
    private static final AtomicInteger index = new AtomicInteger();

    public RtspMessage() {
        this(VERSION_1_0, new HashMap<>());
    }

    public RtspMessage(String version, Map<String, String> headers) {
        this.version = version;
        this.headers = headers;
    }

    /**
     * 版本号
     */
    protected String version;

    /**
     * 序列号
     */
    protected int cSeq;

    /**
     * 会话ID
     */
    protected String session = "";

    /**
     * 请求头
     */
    protected Map<String, String> headers;

    @Override
    public String toObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VERSION_1_0).append(CRLF);
        sb.append(C_SEQ).append(COLON).append(this.cSeq).append(CRLF);
        if (this.session != null && !this.session.equals("")) {
            sb.append(SESSION).append(COLON).append(this.session).append(CRLF);
        }
        return sb.toString();
    }

    /**
     * 获取2字节大小的最新序号，0-65536
     *
     * @return 序号
     */
    public static int getUint16Number() {
        return getNumber(65536);
    }

    /**
     * 自定义最大值的最新序号
     *
     * @param max 最大值
     * @return 序号
     */
    public static int getNumber(int max) {
        int res = index.getAndIncrement();
        if (res >= max) {
            index.set(0);
            res = 0;
        }
        return res;
    }
}
