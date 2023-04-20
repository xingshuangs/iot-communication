package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会话描述协议
 *
 * @author xingshuang
 */
@Data
public class RtspSdp {

    /**
     * 会话描述
     */
    private RtspSdpSession session;

    /**
     * 媒体描述
     */
    private List<RtspSdpMedia> medias = new ArrayList<>();

    public static RtspSdp fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析数据源错误src");
        }
        // v= 字符串的索引，到第一个 m= 为session部分的内容
        int startSession = src.indexOf("v=");
        // m= 字符串的索引，每个 m= 之间为 media部分的内容
        List<Integer> flagAllIndexes = StringSpUtil.findFlagAllIndexes(src, "m=");

        RtspSdp sdp = new RtspSdp();
        // session数据解析
        if (startSession >= 0) {
            String sessionStr = src.substring(startSession, flagAllIndexes.get(0));
            sdp.session = RtspSdpSession.fromString(sessionStr);
        }

        // media数据解析
        if (flagAllIndexes.isEmpty()) {
            return sdp;
        }
        List<String> mediaStrList = new ArrayList<>();
        for (int i = 0; i < flagAllIndexes.size() - 1; i++) {
            mediaStrList.add(src.substring(flagAllIndexes.get(i), flagAllIndexes.get(i + 1)));
        }
        mediaStrList.add(src.substring(flagAllIndexes.get(flagAllIndexes.size() - 1)));
        sdp.medias = mediaStrList.stream().map(RtspSdpMedia::fromString).collect(Collectors.toList());

        return sdp;
    }
}
