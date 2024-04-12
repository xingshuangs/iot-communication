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

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import lombok.Data;

/**
 * 请求头
 *
 * @author xingshuang
 */
@Data
public class McHeader1EReq extends McHeaderReq {

    public McHeader1EReq() {
    }

    public McHeader1EReq(McAccessRoute accessRoute,int timer) {
        this.frameType = EMcFrameType.FRAME_1E;
        this.subHeader = this.frameType.getReqSubHeader();
        this.accessRoute = accessRoute;
        this.monitoringTimer = timer / 250;
    }

    @Override
    public int byteArrayLength() {
        return 1 + this.accessRoute.byteArrayLength() + 2;
    }

    @Override
    public byte[] toByteArray() {
        int length = 1 + this.accessRoute.byteArrayLength();
        return ByteWriteBuff.newInstance(length, true)
                .putByte(this.subHeader)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.monitoringTimer)
                .getData();
    }
}
