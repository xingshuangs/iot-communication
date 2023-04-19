package com.github.xingshuangs.iot.protocol.rtsp.sdp;


import com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute.*;
import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
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
    private Map<String, String> attributes = new HashMap<>();

    public static RtspSdpMedia fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析media部分数据源错误");
        }
        RtspSdpMedia media = new RtspSdpMedia();
        List<String> list = StringSpUtil.splitOneStepByLine(src, CRLF);
        for (String item : list) {
            int i = item.indexOf(EQUAL);
            if (i == -1) {
                continue;
            }
            String key = item.substring(0, i).trim();
            String value = item.substring(i + 1).trim();
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
        }
        return media;
    }

    public RtspSdpMediaAttrControl getAttributeControl() {
        String value = this.attributes.get("control");
        if (value == null) {
            return null;
        }
        return RtspSdpMediaAttrControl.fromString(value);
    }

    public RtspSdpMediaAttrDimension getAttributeDimension() {
        String value = this.attributes.get("x-dimensions");
        if (value == null) {
            return null;
        }
        return RtspSdpMediaAttrDimension.fromString(value);
    }

    public RtspSdpMediaAttrFmtp getAttributeFmtp() {
        String value = this.attributes.get("fmtp");
        if (value == null) {
            return null;
        }
        return RtspSdpMediaAttrFmtp.fromString(value);
    }

    public RtspSdpMediaAttrRtpMap getAttributeRtpMap() {
        String value = this.attributes.get("rtpmap");
        if (value == null) {
            return null;
        }
        return RtspSdpMediaAttrRtpMap.fromString(value);
    }

    public RtspSdpMediaAttrHeader getAttributeHeader() {
        String value = this.attributes.get("Media_header");
        if (value == null) {
            return null;
        }
        return RtspSdpMediaAttrHeader.fromString(value);
    }
}
