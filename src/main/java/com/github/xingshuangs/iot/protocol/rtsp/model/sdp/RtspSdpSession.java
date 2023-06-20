package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.utils.StringSpUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.*;

/**
 * 会话
 *
 * @author xingshuang
 */
@Data
public class RtspSdpSession {

    /**
     * 版本 v（必选） 0
     */
    private Integer version;

    /**
     * 源 o（必选） - 1517245007527432 1517245007527432 IN IP4 10.3.8.202
     */
    private RtspSdpOrigin origin;

    /**
     * 会话名称 s（必选） Media Presentation
     */
    private String sessionName;

    /**
     * 时间（必选）0 0
     */
    private RtspSdpTime time;

    /**
     * 会话信息(可选)
     */
    private String sessionInfo;

    /**
     * 统一资源描述(可选)
     */
    private String uri;

    /**
     * 电子邮件(可选)
     */
    private String email;

    /**
     * 电话号码(可选)
     */
    private String phone;

    /**
     * 连接信息(可选) IN IP4 0.0.0.0
     */
    private RtspSdpConnection connection;

    /**
     * 带宽(可选) AS:5050
     */
    private RtspSdpBandwidth bandwidth;

    /**
     * 重复时间(可选)
     */
    private RtspSdpEchoTime echoTime;

    /**
     * 附加信息(可选)
     */
    private Map<String, String> attributes = new HashMap<>();

    public static RtspSdpSession fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析session部分数据源错误");
        }
        RtspSdpSession session = new RtspSdpSession();
        Map<String, String> map = StringSpUtil.splitTwoStepByLine(src, CRLF, EQUAL);

        map.forEach((key, value) -> {
            switch (key) {
                case "v":
                    session.version = Integer.parseInt(value);
                    break;
                case "o":
                    session.origin = RtspSdpOrigin.fromString(value);
                    break;
                case "s":
                    session.sessionName = value;
                    break;
                case "c":
                    session.connection = RtspSdpConnection.fromString(value);
                    break;
                case "e":
                    session.email = value;
                    break;
                case "b":
                    session.bandwidth = RtspSdpBandwidth.fromString(value);
                    break;
                case "t":
                    session.time = RtspSdpTime.fromString(value);
                    break;
                case "a":
                    int index = value.indexOf(COLON);
                    if (index == -1) {
                        session.attributes.put(value, "");
                    } else {
                        session.attributes.put(value.substring(0, index), value.substring(index + 1));
                    }
                    break;
                default:
                    break;
            }
        });
        return session;
    }
}
