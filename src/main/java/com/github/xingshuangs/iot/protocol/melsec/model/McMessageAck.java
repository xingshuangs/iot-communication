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
 * 响应消息
 *
 * @author xingshuang
 */
@Data
public class McMessageAck implements IObjectByteArray {
    /**
     * 响应头
     */
    private McHeaderAck header;

    /**
     * 响应数据
     */
    private McData data;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + this.data.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putBytes(this.header.toByteArray())
                .putBytes(this.data.toByteArray())
                .getData();
    }

    /**
     * 自我校验，主要核对数据长度
     */
    public void selfCheck() {
        if (this.header.getFrameType() == EMcFrameType.FRAME_1E) {
            return;
        }
        McHeader3EAck header3EAck = (McHeader3EAck) this.header;
        header3EAck.dataLength = 2 + this.data.byteArrayLength();
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      字节数组数据
     * @param frameType 帧类型
     * @return McMessageAck
     */
    public static McMessageAck fromBytes(final byte[] data, EMcFrameType frameType) {
        return fromBytes(data, 0, frameType);
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data      字节数组数据
     * @param offset    偏移量
     * @param frameType 帧类型
     * @return McMessageAck
     */
    public static McMessageAck fromBytes(final byte[] data, final int offset, EMcFrameType frameType) {
        McMessageAck res = new McMessageAck();
        res.header = McHeaderAck.fromBytes(data, offset, frameType);
        int off = offset + res.header.byteArrayLength();
        if (res.header.getEndCode() == 0) {
            res.data = McAckData.fromBytes(data, off);
        } else if (frameType == EMcFrameType.FRAME_1E) {
            res.data = McError1EData.fromBytes(data, off);
        } else {
            res.data = McError4E3EData.fromBytes(data, off);
        }
        return res;
    }
}
