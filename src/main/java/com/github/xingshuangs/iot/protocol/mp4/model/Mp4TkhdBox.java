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
 * tkhd(Track Header) box, contains header information about the media stream, such as trackid, video resolution, and more.
 * 包含关于媒体流的头信息，如trackid，视频分辨率等信息
 *
 * @author xingshuang
 */
public class Mp4TkhdBox extends Mp4Box {

    /**
     * 1-bytes, version
     */
    private final int version;

    /**
     * 3-bytes flags
     */
    private final byte[] flags;

    /**
     * 4-bytes creation time
     */
    private final LocalDateTime creationTime;

    /**
     * 4-bytes modification time
     */
    private final LocalDateTime modificationTime;

    /**
     * 4-bytes timescale
     */
    private final int trackId;

    /**
     * 4个-bytes
     */
    private final byte[] reserve1;

    /**
     * 4-bytes duration
     */
    private final int duration;

    /**
     * 8个-bytes
     */
    private final byte[] reserve2;

    /**
     * 2-bytes the video layer, which is sort before and after, here is all the videos, set it to 0.
     * 视频layer，相当于前后排序，这里就一路视频，设置为0
     */
    private final int layer;

    /**
     * 2个-bytes
     */
    private final byte[] reserve3;

    /**
     * 2个-bytes volume setting
     * 音量设置
     */
    private final int volume;

    /**
     * 2个-bytes
     */
    private final byte[] reserve4;

    /**
     * 36-bytes transformation matrix
     * 变换矩阵
     */
    private final byte[] unityMatrix;

    /**
     * 4-bytes video width
     * 视频宽
     */
    private final int width;

    /**
     * 4-bytes video height
     * 视频高
     */
    private final int height;

    public Mp4TkhdBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.TKHD;
        this.version = 0;
        this.flags = new byte[]{0x00, 0x00, 0x07};
        this.creationTime = TimesUtil.getUTCDateTime(0);
        this.modificationTime = TimesUtil.getUTCDateTime(0);
        this.trackId = trackInfo.getId();
        this.reserve1 = new byte[4];
        this.duration = trackInfo.getDuration();
        this.reserve2 = new byte[8];
        this.layer = trackInfo.getType().equals("video") ? 0 : 1;
        this.reserve3 = new byte[2];
        this.volume = 0;
        this.reserve4 = new byte[2];
        this.unityMatrix = new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00
        };
        this.width = trackInfo.getWidth();
        this.height = trackInfo.getHeight();
    }

    @Override
    public int byteArrayLength() {
        return 92;
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
                .putInteger(this.trackId)
                .putBytes(this.reserve1)
                .putInteger(this.duration)
                .putBytes(this.reserve2)
                .putShort(this.layer)
                .putBytes(this.reserve3)
                .putShort(this.volume)
                .putBytes(this.reserve4)
                .putBytes(this.unityMatrix)
                .putInteger(this.width << 16)
                .putInteger(this.height << 16)
                .getData();
    }
}
