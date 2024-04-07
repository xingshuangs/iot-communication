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

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * 附加信息：媒体负载属性
 * rtpmap:96 H264/90000
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrRtpMap {

    /**
     * 负载编号
     */
    private Integer payloadNumber;

    /**
     * 负载格式
     */
    private String payloadFormat;

    /**
     * 时钟频率
     */
    private Integer clockFrequency;

    /**
     * 声道数量
     */
    private Integer soundTrackNumber;

    public static RtspSdpMediaAttrRtpMap fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("MediaAttrRtpMap data error");
        }
        RtspSdpMediaAttrRtpMap rtpMap = new RtspSdpMediaAttrRtpMap();
        int i = src.indexOf(SP);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrRtpMap data error");
        }
        rtpMap.payloadNumber = Integer.parseInt(src.substring(0, i));
        String sub = src.substring(i + 1).trim();
        String[] split = sub.split("/");
        if (split.length > 0) {
            rtpMap.payloadFormat = split[0];
        }
        if (split.length > 1) {
            rtpMap.clockFrequency = Integer.parseInt(split[1]);
        }
        if (split.length > 2) {
            rtpMap.soundTrackNumber = Integer.parseInt(split[2]);
        }
        return rtpMap;
    }
}
