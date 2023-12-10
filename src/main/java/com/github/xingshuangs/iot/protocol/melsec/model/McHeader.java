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
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import lombok.Data;

/**
 * 协议头
 *
 * @author xingshuang
 */
@Data
public class McHeader implements IObjectByteArray {

    /**
     * 帧类型
     */
    protected EMcFrameType frameType = EMcFrameType.FRAME_3E;

    /**
     * 副帧头，2字节，根据报文的类型定义设置的值。
     */
    protected int subHeader = 0;

    /**
     * 序列号
     */
    protected int serialNumber = 0;

    /**
     * 固定值编号
     */
    protected int fixedNumber = 0;

    /**
     * 访问路径，存在多种访问路径
     */
    protected McAccessRoute accessRoute;

    /**
     * 数据长度，2字节，请求数据长，指定从监视定时器到请求数据为止的数据长；响应数据长，
     * 存储从结束代码到响应数据(正常结束时)或出错信息(异常结束时)为止的数据长。
     */
    protected int dataLength = 0;

    @Override
    public int byteArrayLength() {
        return (frameType == EMcFrameType.FRAME_4E ? 6 : 2) + this.accessRoute.byteArrayLength() + 2;
    }

    @Override
    public byte[] toByteArray() {
        int length = (frameType == EMcFrameType.FRAME_4E ? 6 : 2) + this.accessRoute.byteArrayLength() + 2;
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true)
                .putShort(this.subHeader);
        if (frameType == EMcFrameType.FRAME_4E) {
            buff.putShort(this.serialNumber);
            buff.putShort(this.fixedNumber);
        }
        return buff.putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .getData();
    }

    /**
     * 解析字节数组数据
     *
     * @param data      字节数组数据
     * @param frameType 帧类型
     * @return McHeader
     */
    public static McHeader fromBytes(final byte[] data, EMcFrameType frameType) {
        return fromBytes(data, 0, frameType);
    }

    /**
     * 解析字节数组数据
     *
     * @param data      字节数组数据
     * @param offset    偏移量
     * @param frameType 帧类型
     * @return McHeaderAck
     */
    public static McHeader fromBytes(final byte[] data, final int offset, EMcFrameType frameType) {
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McHeader res = new McHeader();
        res.frameType = frameType;
        res.subHeader = buff.getUInt16();
        if (frameType == EMcFrameType.FRAME_4E) {
            res.serialNumber = buff.getUInt16();
            res.fixedNumber = buff.getUInt16();
        }
        res.accessRoute = McFrame4E3EAccessRoute.fromBytes(buff.getBytes(5));
        res.dataLength = buff.getUInt16();
        return res;
    }
}
