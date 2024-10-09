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


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import lombok.Data;

/**
 * Req header.
 * (请求头)
 *
 * @author xingshuang
 */
@Data
public class McHeaderReq implements IObjectByteArray {

    /**
     * Frame type.
     * (帧类型)
     */
    protected EMcFrameType frameType = EMcFrameType.FRAME_3E;

    /**
     * Sub header, 2-bytes.
     * 副帧头，2字节，根据报文的类型定义设置的值。
     * 1E帧，1个字节
     */
    protected int subHeader = 0;

    /**
     * Access route.
     * 访问路径，存在多种访问路径
     */
    protected McAccessRoute accessRoute;

    /**
     * Monitoring timer.
     * 监视定时器，2字节，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。
     * 0000H(0): 无限等待(处理完成之前继续等待。)<br>
     * 0001H～FFFFH(1～65535): 等待时间(单位: 250ms)<br>
     * 连接站(本站):0001H～0028H(0.25秒～10秒)
     * 其它站:0002H～00F0H(0.5秒～60秒)
     */
    protected int monitoringTimer;

    @Override
    public int byteArrayLength() {
        throw new UnsupportedOperationException("not implement");
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("not implement");
    }

    public static McHeaderReq createByFrameType(EMcFrameType frameType, McAccessRoute accessRoute, int timer) {
        switch (frameType) {
            case FRAME_1E:
                return new McHeader1EReq(accessRoute, timer);
            case FRAME_3E:
                return new McHeader3EReq(accessRoute, timer);
            case FRAME_4E:
                return new McHeader4EReq(accessRoute, timer);
            default:
                throw new McCommException("unknown frame type");
        }
    }
}
