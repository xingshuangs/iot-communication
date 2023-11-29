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

package com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SEMICOLON;

/**
 * 头
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrHeader {

    private String mediaInfo;

    public static RtspSdpMediaAttrHeader fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrHeader部分数据源错误");
        }
        RtspSdpMediaAttrHeader header = new RtspSdpMediaAttrHeader();
        int start = src.indexOf(EQUAL);
        int end = src.indexOf(SEMICOLON);
        if (start == -1) {
            throw new RtspCommException("RtspSdpMediaAttrHeader数据有误，无法解析");
        }
        if (end == -1) {
            header.mediaInfo = src.substring(start + 1);
        } else {
            header.mediaInfo = src.substring(start + 1, end);
        }
        return header;
    }
}
