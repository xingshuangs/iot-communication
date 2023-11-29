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
 * ftyp盒子
 *
 * @author xingshuang
 */
public class Mp4HdlrBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节pre_defined
     */
    private final byte[] preDefined;

    /**
     * 4字节
     */
    private final String handlerType;

    /**
     * 12字节 保留
     */
    private final byte[] reserved;

    /**
     * 5字节 名称
     */
    private final String name;

    public Mp4HdlrBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.HDLR;
        this.version = 0;
        this.flags = new byte[3];
        this.preDefined = new byte[4];
        this.reserved = new byte[12];

        this.handlerType = trackInfo.getType().equals("video") ? "vide" : "soun";
        this.name = trackInfo.getType().equals("video") ? "VideoHandler" : "SoundHandler";
    }

    @Override
    public int byteArrayLength() {
        return 32 + this.name.length() + 1;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putBytes(this.preDefined)
                .putString(this.handlerType)
                .putBytes(this.reserved)
                .putString(this.name)
                .getData();
    }
}
