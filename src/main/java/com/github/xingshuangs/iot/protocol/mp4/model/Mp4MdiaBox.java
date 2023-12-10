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
 * mdia box包含 track 媒体数据信息的 container box,其子box主要包含视频流长度/创建时间/媒体播放过程/sample的偏移量/解码格式等
 *
 * @author xingshuang
 */
public class Mp4MdiaBox extends Mp4Box {

    private final Mp4MdhdBox mdhdBox;

    private final Mp4HdlrBox hdlrBox;

    private final Mp4MinfBox minfBox;

    public Mp4MdiaBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MDIA;
        this.mdhdBox = new Mp4MdhdBox(trackInfo);
        this.hdlrBox = new Mp4HdlrBox(trackInfo);
        this.minfBox = new Mp4MinfBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mdhdBox.byteArrayLength() + this.hdlrBox.byteArrayLength() + this.minfBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mdhdBox.toByteArray())
                .putBytes(this.hdlrBox.toByteArray())
                .putBytes(this.minfBox.toByteArray())
                .getData();
    }
}
