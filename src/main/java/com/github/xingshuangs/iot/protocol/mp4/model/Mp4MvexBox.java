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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Trac kExtends Box (trex) 是 mvex 的子box,用来给fMP4的sample设置默认值。
 *
 * @author xingshuang
 */
public class Mp4MvexBox extends Mp4Box {
    /**
     * 1字节，版本
     */
    private final List<Mp4Box> boxes;

    public Mp4MvexBox(List<Mp4TrackInfo> trackInfos) {
        this.mp4Type = EMp4Type.MVEX;
        this.boxes = trackInfos.stream().map(x -> new Mp4TrexBox(x.getId())).collect(Collectors.toList());
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.boxes.stream().mapToInt(Mp4Box::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray());
        for (Mp4Box box : boxes) {
            buff.putBytes(box.toByteArray());
        }
        return buff.getData();
    }
}
