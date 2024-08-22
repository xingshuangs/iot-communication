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

/**
 * 附加信息：控制部分
 * control:rtsp://10.3.8.202:554/trackID=1
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrControl {

    /**
     * 地址
     */
    private String uri = "";

    /**
     * 轨道ID
     */
    private Integer trackID = 0;

    public static RtspSdpMediaAttrControl fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("MediaAttrControl of Sdp data error");
        }
        RtspSdpMediaAttrControl control = new RtspSdpMediaAttrControl();
        int i = src.indexOf(EQUAL);
        if (i < 0) {
            throw new RtspCommException("RtspSdpMediaAttrControl data error");
        }
        int trackIDIndex = src.indexOf("trackID");
        if (trackIDIndex < 0) {
            return control;
//            throw new RtspCommException("RtspSdpMediaAttrControl data error, trackID is not exist");
        }
        control.uri = src.substring(trackIDIndex);

        String trackIDStr = src.substring(i + 1);
        if (trackIDStr.contains("video")) {
            control.trackID = 0;
        } else if (trackIDStr.contains("audio")) {
            control.trackID = 1;
        } else {
            control.trackID = Integer.parseInt(trackIDStr);
        }
        return control;
    }
}
