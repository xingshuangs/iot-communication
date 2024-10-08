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

package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.SP;

/**
 * Media description.
 * 媒体描述
 * video 0 RTP/AVP 96
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaDesc {

    /**
     * Media type.
     * 媒体类型（必选）video
     */
    private String type;

    /**
     * Port.
     * 端口（必选）0，为什么是0？因为上面在SETUP过程会告知端口号，所以这里就不需要了
     */
    private Integer port;

    /**
     * Protocol (Mandatory) RTP/AVP: RTP OVER UDP. If it is RTP/AVP/TCP: RTP OVER TCP.
     * 协议（必选）RTP/AVP，表示RTP OVER UDP，如果是RTP/AVP/TCP，表示RTP OVER TCP
     */
    private String protocol;

    /**
     * Format type (Mandatory) Indicates the payload type. 96 indicates H.264.
     * 格式类型（必选）表示负载类型(payload type)，一般使用96表示H.264
     */
    private Integer payloadFormatNumber;

    public static RtspSdpMediaDesc fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("The SDP failed to parse some data sources of MediaDesc");
        }
        RtspSdpMediaDesc desc = new RtspSdpMediaDesc();
        String[] split = src.split(SP);
        if (split.length != 4) {
            throw new RtspCommException("RtspSdpMediaDesc data is incorrect and cannot be parsed");
        }
        desc.type = split[0];
        desc.port = Integer.parseInt(split[1]);
        desc.protocol = split[2];
        desc.payloadFormatNumber = Integer.parseInt(split[3]);
        return desc;
    }
}
