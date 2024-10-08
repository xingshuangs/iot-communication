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
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.Base64;
import java.util.List;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * RtspSdpMediaAttrFmtp
 * fmtp:96 profile-level-id=420029; packetization-mode=1; sprop-parameter-sets=Z00AH5Y1QKALdNwEBAQI,aO48gA==
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrFmtp {

    private Integer number;

    private String profileLevelId;

    private Integer packetizationMode;

    /**
     * profile-level-id = "Base16(sps[1])" + "Base16(sps[2])" + "Base16(sps[3])"
     * sprop-parameter-sets = "Base64(sps)" + "," + "Base64(pps)"
     */
    private String spropParameterSets;

    /**
     * SPS
     * 视频中比较重要的SPS
     */
    private byte[] sps;

    /**
     * PPS
     * 视频中比较重要的PPS
     */
    private byte[] pps;

    public static RtspSdpMediaAttrFmtp fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("MediaAttrFmtp of SDP data error");
        }
        RtspSdpMediaAttrFmtp rtpMap = new RtspSdpMediaAttrFmtp();
        int i = src.indexOf(SP);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrFmtp data error");
        }
        rtpMap.number = Integer.parseInt(src.substring(0, i));
        String sub = src.substring(i + 1);
        List<String> stringList = StringSpUtil.splitOneStepByLine(sub, SEMICOLON);
        for (String item : stringList) {
            int i1 = item.indexOf(EQUAL);
            if (i1 == -1) {
                continue;
            }
            String tmp = item.substring(i1 + 1);
            if (item.substring(0, i1).equals("profile-level-id")) {
                rtpMap.profileLevelId = tmp;
            } else if (item.substring(0, i1).equals("packetization-mode")) {
                rtpMap.packetizationMode = Integer.parseInt(tmp);
            } else if (item.substring(0, i1).equals("sprop-parameter-sets")) {
                rtpMap.spropParameterSets = tmp;
                int i2 = tmp.indexOf(",");
                if (i2 >= 0) {
                    Base64.Decoder decoder = Base64.getDecoder();
                    rtpMap.sps = decoder.decode(tmp.substring(0, i2));
                    rtpMap.pps = decoder.decode(tmp.substring(i2 + 1));
                }
            }
        }
        return rtpMap;
    }
}
