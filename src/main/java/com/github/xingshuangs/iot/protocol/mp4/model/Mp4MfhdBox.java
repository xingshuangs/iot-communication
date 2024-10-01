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
 * The Movie Fragment header Box(mfhd), which contains fragment sequence information, identifies the fragment sequence for each track.
 * Movie Fragment header Box(mfhd),包含分片序列信息，标识每个track的分片序列
 *
 * @author xingshuang
 */
public class Mp4MfhdBox extends Mp4Box {

    /**
     * 1-bytes, version
     */
    private final int version;

    /**
     * 3-bytes flags
     */
    private final byte[] flags;

    /**
     * 4-bytes media stream serial number: a serial number for each trunk.
     * The serial number increases from 1, that is, the serial number of each moof, which is 1 more than the previous one.
     * 媒体流序列号，每个trunk一个序列号，序列号从1开始递增，就是每个moof的序列号，相比前一个要加1
     */
    private final long sequenceNumber;

    public Mp4MfhdBox(long sequenceNumber) {
        this.mp4Type = EMp4Type.MFHD;
        this.version = 0;
        this.flags = new byte[3];
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int byteArrayLength() {
        return 16;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.sequenceNumber)
                .getData();
    }
}
