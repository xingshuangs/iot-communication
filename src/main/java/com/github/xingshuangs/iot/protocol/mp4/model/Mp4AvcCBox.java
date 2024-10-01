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

package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description box (stbl-stsd), which stores the description information necessary for decoding,
 * is also a container box. For H264 streams, it contains avc1 subboxes.
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4AvcCBox extends Mp4Box {

    /**
     * 1-bytes, version
     */
    private final int version;

    /**
     * 1-bytes, profile
     */
    private final int profile;

    /**
     * 1-bytes, profile compat
     */
    private final int profileCompat;

    /**
     * 1-bytes, level
     */
    private final int level;

    /**
     * 1 byte nalu length Number of bytes. This bit indicates the length of the nalu after the start bit is removed.
     * The first five digits of this byte are 1 by default. The last three values +1 represent the number of bytes
     * taken up by the frame size, we use 4 bytes to store the frame size, so 3 here, which is 0xFC|0x03=0xFF.
     * 1字节nalu长度头字节数, 此位标识nalu去掉起始位后，前面多少位用来表示nalu长度，此字节前五位默认为1,
     * 后三位值+1表示帧size占用的字节数，我们用4个字节来存储帧大小，所以这里为3，即为0xFC|0x03=0xFF
     */
    private final int lengthSizeMinusOne;

    /**
     * 1 byte number of sps, the first three bits are reserved, the default bit is 1, the last five bits represent
     * the number of sps, we only put one sps, so E0|01=E1.
     * 1字节sps个数，前3位预留，默认位1，后五位表示sps个数，我们只放了一个sps，因此为E0|01=E1
     */
    private final int spsCount;

    /**
     * 2-bytes, sps frame length
     */
    private final int spsLength;

    /**
     * sps content
     */
    private final byte[] sps;

    /**
     * 1-bytes, pps count.
     */
    private final int ppsCount;

    /**
     * 2-bytes, pps frame length
     */
    private final int ppsLength;

    /**
     * pps content.
     */
    private final byte[] pps;

    public Mp4AvcCBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.AVCC;
        this.version = 1;
        this.profile = trackInfo.getSps()[1];
        this.profileCompat = trackInfo.getSps()[2];
        this.level = trackInfo.getSps()[3];
        this.lengthSizeMinusOne = 0xFF;
        this.spsCount = 0xE1;
        this.spsLength = trackInfo.getSps().length;
        this.sps = trackInfo.getSps();
        this.ppsCount = 1;
        this.ppsLength = trackInfo.getPps().length;
        this.pps = trackInfo.getPps();
    }

    @Override
    public int byteArrayLength() {
        return 19 + this.spsLength + this.ppsLength;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putByte(this.profile)
                .putByte(this.profileCompat)
                .putByte(this.level)
                .putByte(this.lengthSizeMinusOne)
                .putByte(this.spsCount)
                .putShort(this.spsLength)
                .putBytes(this.sps)
                .putByte(this.ppsCount)
                .putShort(this.ppsLength)
                .putBytes(this.pps)
                .getData();
    }
}
