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

package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SDP
 *
 * @author xingshuang
 */
@Data
public class RtspSdp {

    /**
     * Session
     * (会话描述)
     */
    private RtspSdpSession session;

    /**
     * Medias
     * (媒体描述)
     */
    private List<RtspSdpMedia> medias = new ArrayList<>();

    public static RtspSdp fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP, src is null or empty");
        }
        // v= 字符串的索引，到第一个 m= 为session部分的内容
        int startSession = src.indexOf("v=");
        // m= 字符串的索引，每个 m= 之间为 media部分的内容
        List<Integer> flagAllIndexes = StringSpUtil.findFlagAllIndexes(src, "\nm=");

        RtspSdp sdp = new RtspSdp();
        // session数据解析
        if (startSession >= 0) {
            String sessionStr = src.substring(startSession, flagAllIndexes.get(0) + 1);
            sdp.session = RtspSdpSession.fromString(sessionStr);
        }

        // media数据解析
        if (flagAllIndexes.isEmpty()) {
            return sdp;
        }
        List<String> mediaStrList = new ArrayList<>();
        for (int i = 0; i < flagAllIndexes.size() - 1; i++) {
            mediaStrList.add(src.substring(flagAllIndexes.get(i) + 1, flagAllIndexes.get(i + 1) + 1));
        }
        mediaStrList.add(src.substring(flagAllIndexes.get(flagAllIndexes.size() - 1) + 1));
        sdp.medias = mediaStrList.stream().map(RtspSdpMedia::fromString).collect(Collectors.toList());

        return sdp;
    }
}
