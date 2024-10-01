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
public class Mp4EsdsBox extends Mp4Box {

    /**
     * 1-bytes
     */
    private final int version;

    /**
     * 3-bytes
     */
    private final byte[] flags;

    /**
     * 1-bytes
     */
    private final int descriptorType1;

    /**
     * 1-bytes
     */
    private final int length1;

    /**
     * 2-bytes
     */
    private final int esId;

    /**
     * 1-bytes
     */
    private final int streamPriority;

    /**
     * 1-bytes
     */
    private final int descriptorType2;

    /**
     * 4-bytes
     */
    private final int length2;

    /**
     * 1-bytes
     */
    private final int codec;

    /**
     * 1-bytes
     */
    private final int streamType;

    /**
     * 3-bytes
     */
    private final byte[] bufferSize;

    /**
     * 4-bytes
     */
    private final int maxBitrate;

    /**
     * 4-bytes
     */
    private final int avgBitrate;

    /**
     * 1-bytes
     */
    private final int descriptorType3;

    /**
     * 1-bytes
     */
    private final int length3;

    private final byte[] config;

    private final byte[] lastData;

    public Mp4EsdsBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.ESDS;
        this.version = 0;
        this.flags = new byte[3];
        this.descriptorType1 = 0x03;
        this.length1 = 0x17 + trackInfo.getConfig().length;
        this.esId = 1;
        this.streamPriority = 0;

        this.descriptorType2 = 0x04;
        this.length2 = 0x0f + trackInfo.getConfig().length;
        this.codec = 0x40;
        this.streamType = 0x15;
        this.bufferSize = new byte[3];
        this.maxBitrate = 0;
        this.avgBitrate = 0;

        this.descriptorType3 = 0x05;
        this.length3 = trackInfo.getConfig().length;
        this.config = trackInfo.getConfig();
        this.lastData = new byte[]{0x06, 0x01, 0x02};
    }

    @Override
    public int byteArrayLength() {
        return 37 + this.config.length;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putByte(this.descriptorType1)
                .putByte(this.length1)
                .putShort(this.esId)
                .putByte(this.streamPriority)
                .putByte(this.descriptorType2)
                .putByte(this.length2)
                .putByte(this.codec)
                .putByte(this.streamType)
                .putBytes(this.bufferSize)
                .putInteger(this.maxBitrate)
                .putInteger(this.avgBitrate)
                .putByte(this.descriptorType3)
                .putByte(this.length3)
                .putBytes(this.config)
                .putBytes(this.lastData)
                .getData();
    }
}
