package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Track Fragment Header box(tfhd),对制定的track进行相关的默认配置，包含trackid、sample时长、大小、偏移量等信息
 *
 * @author xingshuang
 */
public class Mp4TfhdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节trackid，trackid赋值为1，视频track
     */
    private final int trackId;

    public Mp4TfhdBox(int trackId) {
        this.mp4Type = EMp4Type.TFHD;
        this.version = 0;
        this.flags = new byte[3];
        this.trackId = trackId;
    }

    @Override
    public int byteArrayLength() {
        return 16;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.trackId)
                .getData();
    }
}
