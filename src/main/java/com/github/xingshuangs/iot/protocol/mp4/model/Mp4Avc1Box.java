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
 * Sample Description box (stbl-stsd), which stores the description information necessary for decoding,
 * is also a container box. For H264 streams, it contains avc1 subboxes.
 * (Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box)
 *
 * @author xingshuang
 */
public class Mp4Avc1Box extends Mp4Box {

    /**
     * 6-bytes.
     */
    private final byte[] reserved1;

    /**
     * 2-bytes
     */
    private final int dataReferenceIndex;

    /**
     * 2-bytes
     */
    private final byte[] preDefined1;

    /**
     * 2-bytes
     */
    private final byte[] reserved2;

    /**
     * 12-bytes
     */
    private final byte[] preDefined2;

    /**
     * 2-bytes, width
     */
    private final int width;

    /**
     * 2-bytes, height
     */
    private final int height;

    /**
     * 4-bytes, horizontal resolution, default value.
     */
    private final int horizResolution;

    /**
     * 4-bytes, vertical resolution, default value
     */
    private final int vertResolution;

    /**
     * 4-bytes
     */
    private final byte[] reserved3;

    /**
     * 2-bytes, frame count, the fmp4 package is set to 1
     */
    private final int frameCount;

    /**
     * Compress name, name + length = 32 bytes total.
     * (压缩名称，名称+长度=总共32个字节)
     */
    private final byte[] compressNameInfo;

    /**
     * 2-bytes，depth，set to 24
     */
    private final int depth;

    /**
     * 2-bytes
     */
    private final byte[] preDefined3;

    private final Mp4AvcCBox avcCBox;

    private final Mp4BtrtBox btrtBox;

    public Mp4Avc1Box(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.AVC1;
        this.reserved1 = new byte[6];
        this.dataReferenceIndex = 1;
        this.preDefined1 = new byte[2];
        this.reserved2 = new byte[2];
        this.preDefined2 = new byte[12];
        this.width = trackInfo.getWidth();
        this.height = trackInfo.getHeight();
        this.horizResolution = 4_718_592;
        this.vertResolution = 4_718_592;
        this.reserved3 = new byte[4];
        this.frameCount = 1;
        this.compressNameInfo = new byte[]{
                0x12,
                0x62, 0x69, 0x6E, 0x65, //binelpro.ru
                0x6C, 0x70, 0x72, 0x6F,
                0x2E, 0x72, 0x75, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, // compressorname
        };
        this.depth = 24;
        this.preDefined3 = new byte[]{0x11, 0x11};
        this.avcCBox = new Mp4AvcCBox(trackInfo);
        this.btrtBox = new Mp4BtrtBox();
    }

    @Override
    public int byteArrayLength() {
        return 86 + this.avcCBox.byteArrayLength() + this.btrtBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.reserved1)
                .putShort(this.dataReferenceIndex)
                .putBytes(this.preDefined1)
                .putBytes(this.reserved2)
                .putBytes(this.preDefined2)
                .putShort(this.width)
                .putShort(this.height)
                .putInteger(this.horizResolution)
                .putInteger(this.vertResolution)
                .putBytes(this.reserved3)
                .putShort(this.frameCount)
                .putBytes(this.compressNameInfo)
                .putShort(this.depth)
                .putBytes(this.preDefined3)
                .putBytes(this.avcCBox.toByteArray())
                .putBytes(this.btrtBox.toByteArray())
                .getData();
    }
}
