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


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;

import java.util.Collections;
import java.util.List;

/**
 * Mp4 header
 *
 * @author xingshuang
 */
public class Mp4Header implements IObjectByteArray {

    /**
     * Ftyp box
     */
    private final Mp4FtypBox ftypBox;

    /**
     * Moov box
     */
    private final Mp4MoovBox moovBox;

    public Mp4Header(Mp4TrackInfo trackInfo) {
        this.ftypBox = new Mp4FtypBox();
        this.moovBox = new Mp4MoovBox(Collections.singletonList(trackInfo));
    }

    public Mp4Header(List<Mp4TrackInfo> trackInfos) {
        this.ftypBox = new Mp4FtypBox();
        this.moovBox = new Mp4MoovBox(trackInfos);
    }

    @Override
    public int byteArrayLength() {
        return this.ftypBox.byteArrayLength() + this.moovBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putBytes(this.ftypBox.toByteArray())
                .putBytes(this.moovBox.toByteArray())
                .getData();
    }
}
