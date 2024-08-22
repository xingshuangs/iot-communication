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

package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.exceptions.RtcpCommException;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpPackageType;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据包构建器
 *
 * @author xingshuang
 */
public class RtcpPackageBuilder {

    private RtcpPackageBuilder() {
        // NOOP
    }

    public static List<RtcpBasePackage> fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static List<RtcpBasePackage> fromBytes(final byte[] data, final int offset) {
        List<RtcpBasePackage> list = new ArrayList<>();
        int off = offset;
        while (data.length > off) {
            RtcpBasePackage basePackage = parsePackage(data, off);
            list.add(basePackage);
            off += basePackage.byteArrayLength();
        }
        return list;
    }

    public static RtcpBasePackage parsePackage(final byte[] data, final int offset) {
        ERtcpPackageType type = ERtcpPackageType.from(data[1 + offset]);
        if (type == null) {
            throw new RtcpCommException("unrecognized type, byte = " + data[1 + offset]);
        }
        switch (type) {
            case RR:
                return RtcpReceiverReport.fromBytes(data, offset);
            case SR:
                return RtcpSenderReport.fromBytes(data, offset);
            case SDES:
                return RtcpSdesReport.fromBytes(data, offset);
            case BYE:
                return RtcpBye.fromBytes(data, offset);
            case APP:
                return RtcpApp.fromBytes(data, offset);
            default:
                throw new RtcpCommException("unrecognized type");
        }
    }
}
