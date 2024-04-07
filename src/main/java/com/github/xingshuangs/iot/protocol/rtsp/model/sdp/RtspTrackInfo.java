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


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute.RtspSdpMediaAttrDimension;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute.RtspSdpMediaAttrFmtp;
import com.github.xingshuangs.iot.protocol.rtsp.model.sdp.attribute.RtspSdpMediaAttrRtpMap;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.Data;

import java.util.Optional;

/**
 * 轨道信息
 *
 * @author xingshuang
 */
@Data
public class RtspTrackInfo {

    private int id;

    private String type;

    private String codec;

    // region 视频
    private int timescale;

    private int duration;

    private int width;

    private int height;

    private byte[] sps;

    private byte[] pps;

    public static RtspTrackInfo createTrackInfo(RtspSdp sdp) {
        Optional<RtspSdpMedia> optional = sdp.getMedias().stream().filter(x -> x.getMediaDesc().getType().equals("video")).findFirst();
        if (!optional.isPresent()) {
            // 不存在视频相关的SDP
            throw new RtspCommException("No SDP related to video exists");
        }
        RtspSdpMedia media = optional.get();
        RtspTrackInfo trackInfo = new RtspTrackInfo();
        trackInfo.id = media.getAttributeControl().getTrackID();
        trackInfo.type = "video";
        RtspSdpMediaAttrRtpMap rtpMap = media.getAttributeRtpMap();
        trackInfo.timescale = rtpMap.getClockFrequency();
        trackInfo.duration = rtpMap.getClockFrequency();
        if ("H265".equalsIgnoreCase(rtpMap.getPayloadFormat())) {
            // 暂不支持H265协议，可以自行去摄像头设置为H264协议
            throw new RtspCommException("The H265 protocol is not currently supported. You can set the camera to H264");
        }
        RtspSdpMediaAttrDimension dimension = media.getAttributeDimension();
        trackInfo.width = dimension == null ? 1920 : dimension.getWidth();
        trackInfo.height = dimension == null ? 1080 : dimension.getHeight();
        RtspSdpMediaAttrFmtp fmtp = media.getAttributeFmtp();
        trackInfo.sps = fmtp.getSps();
        trackInfo.pps = fmtp.getPps();
        if (trackInfo.sps == null || trackInfo.sps.length < 4) {
            throw new RtspCommException("sps is null or empty");
        }
        ByteReadBuff buff = new ByteReadBuff(trackInfo.sps);
        byte[] bytes = buff.getBytes(1, 3);
        trackInfo.codec = "avc1." + HexUtil.toHexString(bytes, "", false);
        return trackInfo;
    }
}
