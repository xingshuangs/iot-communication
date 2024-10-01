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
 * Sample Description Box(stbl-stsd)，
 * Storing the description information necessary for decoding, it is also a container box, which contains avc1 subboxes for H264 streams.
 * 存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4StsdBox extends Mp4Box {

    /**
     * 1-byte, version
     */
    private final int version;

    /**
     * 3-bytes flags
     */
    private final byte[] flags;

    /**
     * 4-bytes
     */
    private final int entryCount;

    /**
     * entryBox
     */
    private Mp4Box entryBox;

    public Mp4StsdBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.STSD;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 1;
        this.entryBox = trackInfo.getType().equals("video") ? new Mp4Avc1Box(trackInfo) : new Mp4Mp4aBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 16 + this.entryBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.entryCount)
                .putBytes(this.entryBox.toByteArray())
                .getData();
    }
}
