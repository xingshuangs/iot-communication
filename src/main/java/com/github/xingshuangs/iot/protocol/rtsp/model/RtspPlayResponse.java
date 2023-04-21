package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRangeNpt;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspRtpInfo;
import com.github.xingshuangs.iot.protocol.rtsp.model.base.RtspSessionInfo;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SESSION;
import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspResponseHeaderFields.RTP_INFO;

/**
 * Setup响应
 *
 * @author xingshuang
 */
@Getter
public class RtspPlayResponse extends RtspMessageResponse {

    /**
     * RTP相关信息
     * url=rtsp://192.17.1.63:554/trackID=1;seq=3658;rtptime=1710363406,url=rtsp://192.17.1.63:554/trackID=2;seq=6598;rtptime=4065225152
     */
    private List<RtspRtpInfo> rtpInfo = new ArrayList<>();

    /**
     * 特殊的会话信息
     */
    private RtspSessionInfo sessionInfo;

    public static RtspPlayResponse fromHeaderString(String src) {
        if (src == null || src.equals("")) {
            throw new RtspCommException("解析RtspPlayResponse时字符串为空");
        }
        RtspPlayResponse response = new RtspPlayResponse();
        Map<String, String> map = response.parseHeaderAndReturnMap(src);
        if (map.containsKey(RTP_INFO)) {
            List<String> list = StringSpUtil.splitOneStepByLine(map.get(RTP_INFO).trim(), COMMA);
            response.rtpInfo = list.stream().map(RtspRtpInfo::fromString).collect(Collectors.toList());
        }
        // 会话ID
        if (map.containsKey(SESSION)) {
            response.sessionInfo = RtspSessionInfo.fromString(map.get(SESSION).trim());
            response.session = response.sessionInfo.getSessionId();
        }
        return response;
    }

}
