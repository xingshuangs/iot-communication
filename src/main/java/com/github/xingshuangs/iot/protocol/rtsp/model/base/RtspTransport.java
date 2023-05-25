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
    private String protocol = "";

    /**
     * unicast连接方式，单播，如果是multicast则表示多播
     */
    private String castMode = "";

    /**
     * RTP客户端端口号
     */
    private Integer rtpClientPort;

    /**
     * RTCP客户端端口号
     */
    private Integer rtcpClientPort;

    /**
     * RTP服务端端口号
     */
    private Integer rtpServerPort;

    /**
     * RTCP服务端端口号
     */
    private Integer rtcpServerPort;

    private String ssrc = "";

    private String mode = "";

    public static RtspTransport fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspTransport部分数据解析错误");
        }
        RtspTransport transport = new RtspTransport();
        String[] split = src.split(";");
        transport.protocol = split[0];
        transport.castMode = split[1];
        Map<String, String> map = transport.getMap(split);
        if (map.containsKey("client_port")) {
            String clientPort1 = map.get("client_port").trim();
            int clientPortIndex = clientPort1.indexOf("-");
            transport.rtpClientPort = Integer.parseInt(clientPort1.substring(0, clientPortIndex));
            transport.rtcpClientPort = Integer.parseInt(clientPort1.substring(clientPortIndex + 1));
        }
        if (map.containsKey("server_port")) {
            String serverPort1 = map.get("server_port").trim();
            int serverPortIndex = serverPort1.indexOf("-");
            transport.rtpServerPort = Integer.parseInt(serverPort1.substring(0, serverPortIndex));
            transport.rtcpServerPort = Integer.parseInt(serverPort1.substring(serverPortIndex + 1));
        }
        if (map.containsKey("ssrc")) {
            transport.ssrc = map.get("ssrc").trim();
        }
        if (map.containsKey("mode")) {
            transport.mode = map.get("mode").replace("\"", "").trim();
        }
        return transport;
    }

    public Map<String, String> getMap(String[] data) {
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
        res.add(String.format("client_port=%d-%d", this.rtpClientPort, this.rtcpClientPort));

        if (this.rtpServerPort != null && this.rtcpServerPort != null) {
            res.add(String.format("server_port=%d-%d", this.rtpServerPort, this.rtcpServerPort));
        }
        if (this.ssrc != null && !this.ssrc.equals("")) {
            res.add(String.format("ssrc=%s", this.ssrc));
        }
        if (this.mode != null && !this.mode.equals("")) {
            res.add(String.format("mode=\"%s\"", this.mode));
        }
        return String.join(";", res);
    }
}
