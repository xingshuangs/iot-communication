package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
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
            throw new RtspCommException("不存在视频相关的SDP");
        }
        RtspSdpMedia media = optional.get();
        RtspTrackInfo trackInfo = new RtspTrackInfo();
        trackInfo.id = media.getAttributeControl().getTrackID();
        trackInfo.type = "video";
        RtspSdpMediaAttrRtpMap rtpMap = media.getAttributeRtpMap();
        trackInfo.timescale = rtpMap.getClockFrequency();
        trackInfo.duration = rtpMap.getClockFrequency();
        RtspSdpMediaAttrDimension dimension = media.getAttributeDimension();
        trackInfo.width = dimension.getWidth();
        trackInfo.height = dimension.getHeight();
        RtspSdpMediaAttrFmtp fmtp = media.getAttributeFmtp();
        trackInfo.sps = fmtp.getSps();
        trackInfo.pps = fmtp.getPps();
        ByteReadBuff buff = new ByteReadBuff(trackInfo.sps);
        byte[] bytes = buff.getBytes(1, 3);
        trackInfo.codec = "avc1." + HexUtil.toHexString(bytes, "", false);
        return trackInfo;
    }
}
