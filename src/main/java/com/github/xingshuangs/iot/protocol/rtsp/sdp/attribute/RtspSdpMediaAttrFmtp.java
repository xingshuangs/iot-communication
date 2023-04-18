package com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.List;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * RtspSdpMediaAttrFmtp
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrFmtp {

    private Integer number;

    private Integer profileLevelId;

    private Integer packetizationMode;

    private String spropParameterSets;

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
            }
        }
        return rtpMap;
    }
}
