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
 * stsd-avc1-btrt bitrate box描述码率信息，其包含最大码率，平均码率等信息
 *
 * @author xingshuang
 */
public class Mp4BtrtBox extends Mp4Box {

    /**
     * 4字节，缓存大小
     */
    private final int bufferSizeDB;

    /**
     * 4字节为最大码率
     */
    private final int maxBitRate;

    /**
     * 4字节为平均码率
     */
    private final int avgBitRate;

    public Mp4BtrtBox() {
        this.mp4Type = EMp4Type.BTRT;
        this.bufferSizeDB = 1_875_072;
        this.maxBitRate = 3_000_000;
        this.avgBitRate = 3_000_000;
    }

    @Override
    public int byteArrayLength() {
        return 20;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putInteger(this.bufferSizeDB)
                .putInteger(this.maxBitRate)
                .putInteger(this.avgBitRate)
                .getData();
    }
}
