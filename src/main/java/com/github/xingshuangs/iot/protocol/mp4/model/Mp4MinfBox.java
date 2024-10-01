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
 * Media Information Box, which explains handler-specific information about track media data.minf is also a container box, Media Information Box.
 * Media Information Box，解释 track 媒体数据的 handler-specific 信息。minf 同样是个 container box，Media Information Box，
 * 解释 track 媒体数据的 handler-specific 信息。minf 同样是个 container box，其内部需要关注的内容是 stbl，这也是 moov 中最复杂的部分。
 *
 * @author xingshuang
 */
public class Mp4MinfBox extends Mp4Box {

    private final Mp4Box mhdBox;

    private final Mp4DinfBox dinfBox;

    private final Mp4StblBox stblBox;

    public Mp4MinfBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MINF;
        this.mhdBox = trackInfo.getType().equals("video") ? new Mp4VmhdBox() : new Mp4SmhdBox();
        this.dinfBox = new Mp4DinfBox();
        this.stblBox = new Mp4StblBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mhdBox.byteArrayLength() + this.dinfBox.byteArrayLength() + this.stblBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mhdBox.toByteArray())
                .putBytes(this.dinfBox.toByteArray())
                .putBytes(this.stblBox.toByteArray())
                .getData();
    }
}
