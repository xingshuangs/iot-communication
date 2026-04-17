/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>, 王清攀
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
package com.github.xingshuangs.iot.protocol.sl651.service;

import com.github.xingshuangs.iot.protocol.sl651.constant.FrameConstant;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651Response;
import com.github.xingshuangs.iot.utils.CRCUtil;

/**
 * SL651响应报文构建器
 *
 * @author wangqingpan
 */
public class SL651ResponseBuilder {

    /**
     * 构建确认响应报文
     *
     * @param params SL651 响应参数
     * @return 响应报文
     */
    public static byte[] buildAckResponse(SL651Response params) {
        byte[] serialNumber = params.getSerialNumber();
        byte[] timestamp = params.getTimestamp();
        int bodyLen = serialNumber.length + timestamp.length;
        byte[] bodyLenBytes = new byte[]{(byte) bodyLen};
        byte[] responseBytes = new byte[14 + 3 + bodyLen];

        System.arraycopy(FrameConstant.HEADER_START_HEX, 0, responseBytes, 0, 2);
        System.arraycopy(params.getStationAddress(), 0, responseBytes, 2, 5);
        System.arraycopy(params.getCenterStation(), 0, responseBytes, 7, 1);
        System.arraycopy(params.getPassword(), 0, responseBytes, 8, 2);
        System.arraycopy(params.getFunctionCode(), 0, responseBytes, 10, 1);
        System.arraycopy(FrameConstant.HEADER_RSP_DOWN_SYMBOL_AND_ZERO_LEN_HEX, 0, responseBytes, 11, 1);
        System.arraycopy(bodyLenBytes, 0, responseBytes, 12, 1);
        System.arraycopy(FrameConstant.HEADER_RSP_BODY_END_STX_HEX, 0, responseBytes, 13, 1);
        System.arraycopy(serialNumber, 0, responseBytes, 14, 2);
        System.arraycopy(timestamp, 0, responseBytes, 16, 6);
        System.arraycopy(FrameConstant.HEADER_RSP_BODY_END_EOT_HEX, 0, responseBytes, 13 + 1 + bodyLen, 1);

        byte[] crcBytes = CRCUtil.crc16ToByteArray(responseBytes);
        System.arraycopy(crcBytes, 0, responseBytes, 13 + 1 + bodyLen + 1, 2);

        return responseBytes;
    }
}
