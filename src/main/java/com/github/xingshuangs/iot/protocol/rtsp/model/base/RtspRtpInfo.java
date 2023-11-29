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

package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.EQUAL;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SEMICOLON;

/**
 * RTP的信息
 *
 * @author xingshuang
 */
@Data
public class RtspRtpInfo {

    /**
     * 地址，视频播放的地址
     */
    private String url;

    /**
     * 序列号，视频数据包开始的序列号
     */
    private Long seq;

    /**
     * RTP时间，视频开始播放的时间戳
     */
    private Long rtpTime;

    public static RtspRtpInfo fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspRtpInfo部分数据解析错误");
        }
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, SEMICOLON, EQUAL);
        RtspRtpInfo rtpInfo = new RtspRtpInfo();
        rtpInfo.url = map.getOrDefault("url", "");
        if (map.containsKey("seq")) {
            rtpInfo.seq = Long.parseLong(map.get("seq"));
        }
        if (map.containsKey("rtptime")) {
            rtpInfo.rtpTime = Long.parseLong(map.get("rtptime"));
        }
        return rtpInfo;
    }
}
