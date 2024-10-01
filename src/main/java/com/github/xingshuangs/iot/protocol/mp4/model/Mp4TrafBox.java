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
 * Movie Fragment Box(moof) is a box added to data in fmp4. moof and mdat appear in pairs.
 * This box is the description information of video fragments.moof is a top-level box that is also a container box,
 * followed by a mfhd, with no content of its own.
 * Movie Fragment Box(moof),是 fmp4中数据追加的box,moof 和mdat是成对出现的,这个box是视频分片的描述信息，每个Fragment中包含moof
 * 和mdat，这样的结构符合渐进式播放需求。moof是一个顶级box,同时是一个容器box,下面紧跟一个mfhd,自身无内容。
 *
 * @author xingshuang
 */
public class Mp4TrafBox extends Mp4Box {

    /**
     * Track Fragment Header box(tfhd)
     */
    private final Mp4TfhdBox tfhdBox;

    /**
     * Track Fragment Base Media Decode Time Box(tfdt)
     */
    private final Mp4TfdtBox tfdtBox;

    /**
     * Track Fragment Run Box(trun)
     */
    private final Mp4TrunBox trunBox;

    /**
     * Independent and Disposable Samples Box(sdtp)
     */
    private final Mp4SdtpBox sdtpBox;

    public Mp4TrafBox(long baseMediaDecodeTime, Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.TRAF;
        this.tfhdBox = new Mp4TfhdBox(trackInfo.getId());
        this.tfdtBox = new Mp4TfdtBox(baseMediaDecodeTime);
        this.trunBox = new Mp4TrunBox(trackInfo.getSampleData());
        this.sdtpBox = new Mp4SdtpBox(trackInfo.getSampleData());
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.tfhdBox.byteArrayLength() + this.tfdtBox.byteArrayLength()
                + this.trunBox.byteArrayLength() + this.sdtpBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.tfhdBox.toByteArray())
                .putBytes(this.tfdtBox.toByteArray())
                .putBytes(this.trunBox.toByteArray())
                .putBytes(this.sdtpBox.toByteArray())
                .getData();
    }
}
