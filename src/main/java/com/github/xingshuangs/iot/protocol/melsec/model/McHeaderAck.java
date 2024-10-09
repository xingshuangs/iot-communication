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
 * Ack header.
 * 响应头
 *
 * @author xingshuang
 */
@Data
public class McHeaderAck implements IObjectByteArray {

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
     * End code, 2-bytes.
     * 结束代码，2字节， 1E帧只有1个字节
     * 存储指令处理结果。
     * 正常结束时将存储0。
     * 异常结束时将存储访问目标的出错代码。
     * 出错代码表示发生的出错内容。
     * 同时发生了多个出错的情况下，将返回最初检测的出错代码。
     */
    protected int endCode;

    @Override
    public int byteArrayLength() {
        throw new UnsupportedOperationException("未实现");
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      byte array
     * @param frameType frame type
     * @return McHeaderAck
     */
    public static McHeaderAck fromBytes(final byte[] data, EMcFrameType frameType) {
        return fromBytes(data, 0, frameType);
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      byte array
     * @param offset    index offset
     * @param frameType frame type
     * @return McHeaderAck
     */
    public static McHeaderAck fromBytes(final byte[] data, final int offset, EMcFrameType frameType) {
        switch (frameType) {
            case FRAME_1E:
                return McHeader1EAck.fromBytes(data, offset);
            case FRAME_3E:
                return McHeader3EAck.fromBytes(data, offset);
            case FRAME_4E:
                return McHeader4EAck.fromBytes(data, offset);
            default:
                throw new McCommException("unknown frame type");
        }
    }
}
