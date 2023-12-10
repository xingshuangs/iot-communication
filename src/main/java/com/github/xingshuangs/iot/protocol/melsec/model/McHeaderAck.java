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


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import lombok.Data;

/**
 * 响应头
 *
 * @author xingshuang
 */
@Data
public class McHeaderAck extends McHeader {

    /**
     * 结束代码，2字节
     * 存储指令处理结果。
     * 正常结束时将存储0。
     * 异常结束时将存储访问目标的出错代码。
     * 出错代码表示发生的出错内容。
     * 同时发生了多个出错的情况下，将返回最初检测的出错代码。
     */
    private int endCode;

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
                .putShort(this.endCode)
                .getData();
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return McHeaderAck
     */
    public static McHeaderAck fromBytes(final byte[] data, EMcFrameType frameType) {
        return fromBytes(data, 0, frameType);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return McHeaderAck
     */
    public static McHeaderAck fromBytes(final byte[] data, final int offset, EMcFrameType frameType) {
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McHeaderAck res = new McHeaderAck();
        res.frameType = frameType;
        res.subHeader = buff.getUInt16();
        if (frameType == EMcFrameType.FRAME_4E) {
            res.serialNumber = buff.getUInt16();
            res.fixedNumber = buff.getUInt16();
        }
        res.accessRoute = McFrame4E3EAccessRoute.fromBytes(buff.getBytes(5));
        res.dataLength = buff.getUInt16();
        res.endCode = buff.getUInt16();
        return res;
    }
}
