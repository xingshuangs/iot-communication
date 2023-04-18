package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * 媒体描述
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMedia {

    /**
     * 媒体描述
     */
    private RtspSdpMediaDesc mediaDesc;

    /**
     * 连接信息(可选)
     */
    private RtspSdpConnection connection;

    /**
     * 带宽(可选)
     */
    private RtspSdpBandwidth bandwidth;

    /**
     * 加密秘钥(可选)
     */
    private RtspSdpEncryptKey encryptKey;

    /**
     * 附加信息(可选)
     */
    private Map<String,String> attributes;

    public static RtspSdpMedia fromString(String src){
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析media部分数据源错误");
        }
        RtspSdpMedia media = new RtspSdpMedia();
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, CRLF, EQUAL);

        map.forEach((key, value) -> {
            switch (key) {
                case "m":
                    media.mediaDesc = RtspSdpMediaDesc.fromString(value);
                    break;
                case "c":
                    media.connection = RtspSdpConnection.fromString(value);
                    break;
                case "b":
                    media.bandwidth = RtspSdpBandwidth.fromString(value);
                    break;
                case "a":
                    int index = value.indexOf(COLON);
                    if (index == -1) {
                        media.attributes.put(value, "");
                    } else {
                        media.attributes.put(value.substring(0, index), value.substring(index + 1));
                    }
                    break;
                default:
                    break;
            }
        });
        return media;
    }

}
