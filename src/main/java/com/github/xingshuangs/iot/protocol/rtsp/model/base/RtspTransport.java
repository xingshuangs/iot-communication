package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transport: RTP/AVP;unicast;client_port=57844-57845
 *
 * @author xingshuang
 */
@Data
public class RtspTransport {

    /**
     * 协议RTP/AVP，RTP/AVP：表示RTP通过UDP发送，如果是RTP/AVP/TCP则表示RTP通过TCP发送
     */
    protected String protocol = "";

    /**
     * unicast连接方式，单播，如果是multicast则表示多播
     */
    protected String castMode = "";

    /**
     * id
     */
    protected String ssrc = "";

    /**
     * 模式
     */
    protected String mode = "";

    public static RtspTransport fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspTransport部分数据解析错误");
        }
        if (src.contains("interleaved")) {
            return RtspInterleavedTransport.fromString(src);
        } else {
            return RtspClientPortTransport.fromString(src);
        }
    }

    protected Map<String, String> getMap(String[] data) {
        Map<String, String> res = new HashMap<>();
        for (String item : data) {
            int index = item.indexOf("=");
            if (index >= 0) {
                res.put(item.substring(0, index).trim(), item.substring(index + 1).trim());
            }
        }
        return res;
    }

    @Override
    public String toString() {
        if (this.protocol == null || this.protocol.equals("")) {
            throw new RtspCommException("RtspTransport的protocol异常");
        }
        if (this.castMode == null || this.castMode.equals("")) {
            throw new RtspCommException("RtspTransport的castMode异常");
        }
        List<String> res = new ArrayList<>();
        res.add(this.protocol);
        res.add(this.castMode);

        if (this.ssrc != null && !this.ssrc.equals("")) {
            res.add(String.format("ssrc=%s", this.ssrc));
        }
        if (this.mode != null && !this.mode.equals("")) {
            res.add(String.format("mode=\"%s\"", this.mode));
        }
        return String.join(";", res);
    }
}
