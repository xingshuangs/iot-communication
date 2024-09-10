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

package com.github.xingshuangs.iot.protocol.rtp.model.frame;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EFrameType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264SliceType;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.ExpGolomb;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluBuilder;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluSingle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 普通帧
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class H264VideoFrame extends RawFrame {

    /**
     * 起始分割符
     */
    public static final byte[] START_MARKER = new byte[]{0x00, 0x00, 0x00, 0x01};

    private final EH264NaluType naluType;

    private EH264SliceType sliceType;

    public H264VideoFrame(EH264NaluType naluType, long timestamp, byte[] frameSegment) {
        this.frameType = EFrameType.VIDEO;
        this.naluType = naluType;
        this.timestamp = timestamp;
        this.frameSegment = frameSegment;
        if (naluType == EH264NaluType.IDR_SLICE || naluType == EH264NaluType.NON_IDR_SLICE) {
            ByteReadBuff buff = ByteReadBuff.newInstance(frameSegment);
            buff.getByte();
            // 从第二个字节开始，取2个字节
            ExpGolomb expGolomb = new ExpGolomb(buff.getBytes(2));
            expGolomb.readUE();
            int type = expGolomb.readUE();
            this.sliceType = EH264SliceType.from(type % 5);
        }
    }

    @Override
    public int byteArrayLength() {
        return 6 + this.frameSegment.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.frameType.getCode())
                .putByte(this.naluType.getCode())
                .putInteger(this.timestamp)
                .putBytes(this.frameSegment)
                .getData();
    }

    /**
     * 创建
     *
     * @param frameSegment 帧字节数组
     * @return 视频帧
     */
    public static H264VideoFrame createSpsPpsFrame(byte[] frameSegment) {
        H264NaluSingle naluSingle = (H264NaluSingle) H264NaluBuilder.parsePackage(frameSegment);
        return new H264VideoFrame(naluSingle.getHeader().getType(), System.currentTimeMillis(), naluSingle.toByteArray());
    }
}
