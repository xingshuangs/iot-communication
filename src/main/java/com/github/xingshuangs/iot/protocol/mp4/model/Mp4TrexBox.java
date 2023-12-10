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
 * Trac kExtends Box (trex) 是 mvex 的子box,用来给fMP4的sample设置默认值。
 *
 * @author xingshuang
 */
public class Mp4TrexBox extends Mp4Box {
    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节
     */
    private final int trackId;

    /**
     * 4字节
     */
    private final int defaultSampleDescriptionIndex;

    /**
     * 4字节
     */
    private final int defaultSampleDuration;

    /**
     * 4字节
     */
    private final int defaultSampleSize;

    /**
     * 4字节
     */
    private final int defaultSampleFlags;

    public Mp4TrexBox(int trackId) {
        this.mp4Type = EMp4Type.TREX;
        this.version = 0;
        this.flags = new byte[3];
        this.trackId = trackId;
        this.defaultSampleDescriptionIndex = 1;
        this.defaultSampleDuration = 0;
        this.defaultSampleSize = 0;
        this.defaultSampleFlags = 0x010001;
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.trackId)
                .putInteger(this.defaultSampleDescriptionIndex)
                .putInteger(this.defaultSampleDuration)
                .putInteger(this.defaultSampleSize)
                .putInteger(this.defaultSampleFlags)
                .getData();
    }
}
