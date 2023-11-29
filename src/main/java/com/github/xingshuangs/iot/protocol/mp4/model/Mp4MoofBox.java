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


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * mdat盒子
 *
 * @author xingshuang
 */
public class Mp4MoofBox extends Mp4Box {

    /**
     * Movie Fragment header Box(mfhd)
     */
    private final Mp4MfhdBox mfhdBox;

    /**
     * Track Fragment Box(traf)
     */
    private final Mp4TrafBox trafBox;

    public Mp4MoofBox(long sequenceNumber, long baseMediaDecodeTime, Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MOOF;
        this.mfhdBox = new Mp4MfhdBox(sequenceNumber);
        this.trafBox = new Mp4TrafBox(baseMediaDecodeTime, trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mfhdBox.byteArrayLength() + this.trafBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mfhdBox.toByteArray())
                .putBytes(this.trafBox.toByteArray())
                .getData();
    }
}
