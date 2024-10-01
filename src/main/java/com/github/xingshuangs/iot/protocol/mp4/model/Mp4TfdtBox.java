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
 * Track Fragment Base Media Decode Time Box(tfdt), it is mainly used to store the absolute time of the relevant sample code.
 * 主要是用来存放相关sample编码的绝对时间的,
 * 因为FMP4是流式的格式，所以不像MP4一样可以直接根据sample直接seek到具体位置。
 *
 * @author xingshuang
 */
public class Mp4TfdtBox extends Mp4Box {

    /**
     * 1 byte, version, if version = 0, baseMediaDecodeTime length = 4 bytes, if version = 1, baseMediaDecodeTime length = 8 bytes.
     * 1字节，版本，若version=0，baseMediaDecodeTime长度=4字节，若version=1，baseMediaDecodeTime长度=8字节
     */
    private final int version;

    /**
     * 3-bytes flags
     */
    private final byte[] flags;

    /**
     * 4 or 8-byte Base media decode time, starting from 0, mainly calculates the time offset of each fragment,
     * so it is equal to the sum of the previous baseline time + the duration of all samples of the previous fragment.
     * The baseline time of the first fragment is 0, for example, each fragment contains one sample.Each sample has a duration of 3600,
     * so the baseline decoding time for the first shard is 0.
     * 4或8字节Base media decode time，基准解码时间，从0开始，主要计算每个分片的时间偏移，所以其等于前一个基准时间+前一个分片所有sample
     * 持续时间的总和，第一个分片的基准时间为0，例如每个分片包含一个sample，每个sample持续时间为3600，那么第一个分片的基准解码时间为0.
     * 第二个分片为0+3600=3600，第三个分片等于3600+3600=7200，如此类推
     * 当baseMediaDecodeTime长度=4字节，最大可播放时间是13小时多一点，当baseMediaDecodeTime长度=8字节，没有限制了，但是得看MSE是否支持
     */
    private final long baseMediaDecodeTime;

    public Mp4TfdtBox(long baseMediaDecodeTime) {
        this.mp4Type = EMp4Type.TFDT;
        this.version = 0;
        this.flags = new byte[3];
        this.baseMediaDecodeTime = baseMediaDecodeTime;
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
                .putInteger(this.baseMediaDecodeTime)
                .getData();
    }
}
