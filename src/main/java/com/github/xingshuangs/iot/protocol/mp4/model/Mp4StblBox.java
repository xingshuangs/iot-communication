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
 * The Sample Table Box(stbl) is the most important part of the mdia and stores information about each Sample in the file.
 * First, introduce the concepts of Sample and trunk.
 * Sample Table Box(stbl)是mdia中最主要的部分，存放文件中每个 Sample信息.首先介绍下Sample和trunk的概念，
 * 在 MP4文件中，Sample 是一个媒体流的基本单元
 *
 * @author xingshuang
 */
public class Mp4StblBox extends Mp4Box {

    private final Mp4StsdBox stsdBox;

    private final Mp4SttsBox sttsBox;

    private final Mp4StscBox stscBox;

    private final Mp4StszBox stszBox;

    private final Mp4StcoBox stcoBox;

    public Mp4StblBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.STBL;
        this.stsdBox = new Mp4StsdBox(trackInfo);
        this.sttsBox = new Mp4SttsBox();
        this.stscBox = new Mp4StscBox();
        this.stszBox = new Mp4StszBox();
        this.stcoBox = new Mp4StcoBox();
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.stsdBox.byteArrayLength() + this.sttsBox.byteArrayLength()
                + this.stscBox.byteArrayLength() + this.stszBox.byteArrayLength() + this.stcoBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.stsdBox.toByteArray())
                .putBytes(this.sttsBox.toByteArray())
                .putBytes(this.stscBox.toByteArray())
                .putBytes(this.stszBox.toByteArray())
                .putBytes(this.stcoBox.toByteArray())
                .getData();
    }
}
