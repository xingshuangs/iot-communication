/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

    public RtspInterleavedTransport() {
    }

    public RtspInterleavedTransport(Integer interleaved1, Integer interleaved2) {
        this.protocol = "RTP/AVP/TCP";
        this.castMode = "unicast";
        this.interleaved1 = interleaved1;
        this.interleaved2 = interleaved2;
    }

    public static RtspInterleavedTransport fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspTransport, src is null or empty");
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
            throw new RtspCommException("protocol of RtspTransport is null or empty");
        }
        if (this.castMode == null || this.castMode.equals("")) {
            throw new RtspCommException("castMode of RtspTransport is null or empty");
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
