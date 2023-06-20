package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;
import com.github.xingshuangs.iot.utils.TimesUtil;

import java.time.LocalDateTime;

/**
 * tkhd(Track Header) box包含关于媒体流的头信息，如trackid，视频分辨率等信息
 *
 * @author xingshuang
 */
public class Mp4TkhdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节创建时间
     */
    private final LocalDateTime creationTime;

    /**
     * 4字节修改时间
     */
    private final LocalDateTime modificationTime;

    /**
     * 4字节timescale
     */
    private final int trackId;

    /**
     * 4个字节预留
     */
    private final byte[] reserve1;

    /**
     * 4字节duration
     */
    private final int duration;

    /**
     * 8个字节预留
     */
    private final byte[] reserve2;

    /**
     * 2字节 视频layer，相当于前后排序，这里就一路视频，设置为0
     */
    private final int layer;

    /**
     * 2个字节预留
     */
    private final byte[] reserve3;

    /**
     * 2个字节 音量设置
     */
    private final int volume;

    /**
     * 2个字节预留
     */
    private final byte[] reserve4;

    /**
     * 36字节 变换矩阵
     */
    private final byte[] unityMatrix;

    /**
     * 4字节视频宽
     */
    private final int width;

    /**
     * 4字节视频高
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
