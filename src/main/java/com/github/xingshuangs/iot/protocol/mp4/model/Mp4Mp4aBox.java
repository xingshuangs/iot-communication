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
 * Sample Description box (stbl-stsd), which stores the description information necessary for decoding, is also a container box. For H264 streams, it contains avc1 subboxes.
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4Mp4aBox extends Mp4Box {

    /**
     * 6-bytes
     */
    private final byte[] reserved1;

    /**
     * 2-bytes
     */
    private final int dataReferenceIndex;

    /**
     * 8-bytes
     */
    private final byte[] reserved2;

    /**
     * 2-bytes channel count.
     */
    private final int channelCount;

    /**
     * 2-bytes
     */
    private final int sampleSize;

    /**
     * 2-bytes
     */
    private final byte[] preDefined;

    /**
     * 2-bytes
     */
    private final byte[] reserved3;

    /**
     * 2-bytes
     */
    private final int audioSampleRate;

    /**
     * 2-bytes
     */
    private final byte[] reserved4;

    private final Mp4EsdsBox esdsBox;

    public Mp4Mp4aBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MP4A;
        this.reserved1 = new byte[6];
        this.dataReferenceIndex = 1;
        this.reserved2 = new byte[8];
        this.channelCount = trackInfo.getChannelCount();
        this.sampleSize = 16;
        this.preDefined = new byte[2];
        this.reserved3 = new byte[2];
        this.audioSampleRate = trackInfo.getAudioSampleRate();
        this.reserved4 = new byte[2];

        this.esdsBox = new Mp4EsdsBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 36 + this.esdsBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.reserved1)
                .putShort(this.dataReferenceIndex)
                .putBytes(this.reserved2)
                .putShort(this.channelCount)
                .putShort(this.sampleSize)
                .putBytes(this.preDefined)
                .putBytes(this.reserved3)
                .putShort(this.audioSampleRate)
                .putBytes(this.reserved4)
                .putBytes(this.esdsBox.toByteArray())
                .getData();
    }
}
