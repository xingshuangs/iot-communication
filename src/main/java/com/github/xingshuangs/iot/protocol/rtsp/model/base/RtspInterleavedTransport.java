package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Transport: RTP/AVP/TCP;unicast;interleaved=0-1
 * Transport: RTP/AVP/TCP;unicast;interleaved=0-1;ssrc=1fc17e75;mode="play"
 *
 * @author xingshuang
 */
@Data
public class RtspInterleavedTransport extends RtspTransport {

    /**
     * 交错编号1
     */
    private Integer interleaved1 = 0;

    /**
     * 交错编号2
     */
    private Integer interleaved2 = 1;

    public static RtspInterleavedTransport fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspTransport部分数据解析错误");
        }
        RtspInterleavedTransport transport = new RtspInterleavedTransport();
        String[] split = src.split(";");
        transport.protocol = split[0];
        transport.castMode = split[1];
        Map<String, String> map = transport.getMap(split);
        if (map.containsKey("interleaved")) {
            String clientPort1 = map.get("interleaved").trim();
            int clientPortIndex = clientPort1.indexOf("-");
            transport.interleaved1 = Integer.parseInt(clientPort1.substring(0, clientPortIndex));
            transport.interleaved2 = Integer.parseInt(clientPort1.substring(clientPortIndex + 1));
        }
        if (map.containsKey("ssrc")) {
            transport.ssrc = map.get("ssrc").trim();
        }
        if (map.containsKey("mode")) {
            transport.mode = map.get("mode").replace("\"", "").trim();
        }
        return transport;
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
        res.add(String.format("interleaved=%d-%d", this.interleaved1, this.interleaved2));

        if (this.ssrc != null && !this.ssrc.equals("")) {
            res.add(String.format("ssrc=%s", this.ssrc));
        }
        if (this.mode != null && !this.mode.equals("")) {
            res.add(String.format("mode=\"%s\"", this.mode));
        }
        return String.join(";", res);
    }
}
