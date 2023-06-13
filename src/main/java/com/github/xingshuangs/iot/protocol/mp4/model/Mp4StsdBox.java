package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4StsdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节
     */
    private final int entryCount;

    /**
     * 待定
     */
    private Mp4Box entryBox;

    public Mp4StsdBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.STSD;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 1;
        this.entryBox = trackInfo.getType().equals("video") ? new Mp4Avc1Box(trackInfo) : new Mp4Mp4aBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 16 + this.entryBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.entryCount)
                .putBytes(this.entryBox.toByteArray())
                .getData();
    }
}
