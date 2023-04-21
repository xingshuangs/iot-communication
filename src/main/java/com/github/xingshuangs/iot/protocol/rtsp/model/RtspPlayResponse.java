package com.github.xingshuangs.iot.protocol.rtsp.model;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COMMA;
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
        return response;
    }

}
