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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求头
 *
 * @author xingshuang
 */
@Data
public class McHeaderReq extends McHeader {

    private static final AtomicInteger index = new AtomicInteger();

    /**
     * 监视定时器，2字节，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。
     * 0000H(0): 无限等待(处理完成之前继续等待。)<br>
     * 0001H～FFFFH(1～65535): 等待时间(单位: 250ms)<br>
     * 连接站(本站):0001H～0028H(0.25秒～10秒)
     * 其它站:0002H～00F0H(0.5秒～60秒)
     */
    private int monitoringTimer;

    public McHeaderReq() {
    }

    public McHeaderReq(int subHeader, McAccessRoute accessRoute, int timer) {
        this.subHeader = subHeader;
        this.accessRoute = accessRoute;
        this.monitoringTimer = timer / 250;
        this.serialNumber = getNewNumber();
    }

    /**
     * 获取新的pduNumber
     *
     * @return 编号
     */
    public static int getNewNumber() {
        int res = index.getAndIncrement();
        if (res >= 65536) {
            index.set(0);
            res = 0;
        }
        return res;
    }

    @Override
    public int byteArrayLength() {
        return (frameType == EMcFrameType.FRAME_4E ? 6 : 2) + this.accessRoute.byteArrayLength() + 2 + 2;
    }

    @Override
    public byte[] toByteArray() {
        int length = (frameType == EMcFrameType.FRAME_4E ? 6 : 2) + this.accessRoute.byteArrayLength() + 2 + 2;
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true)
                .putShort(this.subHeader);

        if (frameType == EMcFrameType.FRAME_4E) {
            buff.putShort(this.serialNumber);
            buff.putShort(this.fixedNumber);
        }
        return buff.putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .putShort(this.monitoringTimer)
                .getData();
    }
}
