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
     * Protocol.
     * 协议RTP/AVP，RTP/AVP：表示RTP通过UDP发送，如果是RTP/AVP/TCP则表示RTP通过TCP发送
     */
    protected String protocol = "";

    /**
     * Cast mode.
     * unicast连接方式，单播，如果是multicast则表示多播
     */
    protected String castMode = "";

    /**
     * id
     */
    protected String ssrc = "";

    /**
     * mode.
     */
    protected String mode = "";

    public static RtspTransport fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("RtspTransport, src is null or empty");
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
            throw new RtspCommException("protocol of RtspTransport is null or empty");
        }
        if (this.castMode == null || this.castMode.equals("")) {
            throw new RtspCommException("castMode of RtspTransport is null or empty");
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
