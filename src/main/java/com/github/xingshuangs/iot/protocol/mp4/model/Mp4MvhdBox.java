package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;
import com.github.xingshuangs.iot.utils.TimesUtil;

import java.time.LocalDateTime;

/**
 * ftyp盒子
 *
 * @author xingshuang
 */
public class Mp4MvhdBox extends Mp4Box {

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
    private final int timescale;

    /**
     * 4字节duration
     */
    private final int duration;

    /**
     * 4字节播放速率
     */
    private final int rate;

    /**
     * 2字节音量
     */
    private final int volume;

    /**
     * 10字节预留
     */
    private final byte[] reserved;

    /**
     * 36字节 unity matrix
     */
    private final byte[] videoTransformationMatrix;

    /**
     * 24字节pre_defined
     */
    private final byte[] preDefined;

    /**
     * 4字节next_track_ID
     */
    private final int nextTrackId;

    public  Mp4MvhdBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MVHD;
        this.version = 0;
        this.flags = new byte[3];
        this.creationTime = TimesUtil.getUTCDateTime(1);
        this.modificationTime = TimesUtil.getUTCDateTime(2);
        this.timescale = trackInfo.getTimescale();
        this.duration = trackInfo.getDuration();
        this.rate = 1;
        this.volume = 1;
        this.reserved = new byte[10];
        this.videoTransformationMatrix = new byte[]{
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
        this.preDefined = new byte[24];
        this.nextTrackId = 0xFFFFFFFF;
    }

    @Override
    public int byteArrayLength() {
        return 108;
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
                .putInteger(this.rate<<16)
                .putShort(this.volume<<8)
                .putBytes(this.reserved)
                .putBytes(this.videoTransformationMatrix)
                .putBytes(this.preDefined)
                .putInteger(this.nextTrackId)
                .getData();
    }
}
