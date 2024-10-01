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
import com.github.xingshuangs.iot.utils.TimesUtil;

import java.time.LocalDateTime;

/**
 * Media Header Box: Stores the video stream creation time and length.
 * Media Header Box,存放视频流创建时间，长度等信息
 *
 * @author xingshuang
 */
public class Mp4MdhdBox extends Mp4Box {

    /**
     * 1-bytes, version
     */
    private final int version;

    /**
     * 3-bytes flags
     */
    private final byte[] flags;

    /**
     * 4-bytes creation time.
     */
    private final LocalDateTime creationTime;

    /**
     * 4-bytes modification time
     */
    private final LocalDateTime modificationTime;

    /**
     * 4-byte stimescale
     */
    private final int timescale;

    /**
     * 4-bytes duration
     */
    private final int duration;

    /**
     * 2-bytes language，supported languages, ISO-639-2/T language code.支持的语言，ISO-639-2/T 语言代码
     */
    private final byte[] language;

    /**
     * 2-bytes pre_defined
     */
    private final byte[] preDefined;

    public Mp4MdhdBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MDHD;
        this.version = 0;
        this.flags = new byte[3];
        this.creationTime = TimesUtil.getUTCDateTime(2);
        this.modificationTime = TimesUtil.getUTCDateTime(3);
        this.timescale = trackInfo.getTimescale();
        this.duration = trackInfo.getDuration();
        this.language = new byte[]{0x55, (byte) 0xC4};
        this.preDefined = new byte[2];
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(TimesUtil.getUTCTotalSecond(this.creationTime))
                .putInteger(TimesUtil.getUTCTotalSecond(this.modificationTime))
                .putInteger(this.timescale)
                .putInteger(this.duration)
                .putBytes(this.language)
                .putBytes(this.preDefined)
                .getData();
    }
}
