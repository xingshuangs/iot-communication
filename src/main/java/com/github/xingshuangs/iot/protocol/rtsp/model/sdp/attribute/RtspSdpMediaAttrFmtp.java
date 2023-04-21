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

    private Integer profileLevelId;

    private Integer packetizationMode;

    /**
     * profile-level-id = "Base16(sps[1])" + "Base16(sps[2])" + "Base16(sps[3])"
     * sprop-parameter-sets = "Base64(sps)" + "," + "Base64(pps)"
     */
    private String spropParameterSets;

    /**
     * 视频中比较重要的SPS
     */
    private byte[] sps;

    /**
     * 视频中比较重要的PPS
     */
    private byte[] pps;

    public static RtspSdpMediaAttrFmtp fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrFmtp部分数据源错误");
        }
        RtspSdpMediaAttrFmtp rtpMap = new RtspSdpMediaAttrFmtp();
        int i = src.indexOf(SP);
        if (i == -1) {
            throw new RtspCommException("RtspSdpMediaAttrFmtp数据有误，无法解析");
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
                rtpMap.profileLevelId = Integer.parseInt(tmp);
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
