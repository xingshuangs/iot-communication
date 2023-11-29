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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.ByteUtil;

import java.util.Arrays;

/**
 * @author xingshuang
 */
public class COTPBuilder {

    private COTPBuilder() {
        // NOOP
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return COTP
     */
    public static COTP fromBytes(final byte[] data) {
        int length = ByteUtil.toUInt8(data[0]);
        byte[] cotpBytes = Arrays.copyOfRange(data, 0, length + 1);

        EPduType pduType = EPduType.from(cotpBytes[1]);

        switch (pduType) {
            case CONNECT_REQUEST:
            case CONNECT_CONFIRM:
            case DISCONNECT_REQUEST:
            case DISCONNECT_CONFIRM:
                return COTPConnection.fromBytes(cotpBytes);
            case REJECT:
                return null;
            case DT_DATA:
                return COTPData.fromBytes(cotpBytes);
            default:
                throw new S7CommException("COTP的pduType数据类型无法解析");
        }
    }
}
