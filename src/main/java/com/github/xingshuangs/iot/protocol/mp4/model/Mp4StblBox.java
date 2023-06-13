package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Table Box(stbl)是mdia中最主要的部分，存放文件中每个 Sample信息.首先介绍下Sample和trunk的概念，
 * 在 MP4文件中，Sample 是一个媒体流的基本单元
 *
 * @author xingshuang
 */
public class Mp4StblBox extends Mp4Box {

    private final Mp4StsdBox stsdBox;

    private final Mp4SttsBox sttsBox;

    private final Mp4StscBox stscBox;

    private final Mp4StszBox stszBox;

    private final Mp4StcoBox stcoBox;

    public Mp4StblBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.STBL;
        this.stsdBox = new Mp4StsdBox(trackInfo);
        this.sttsBox = new Mp4SttsBox();
        this.stscBox = new Mp4StscBox();
        this.stszBox = new Mp4StszBox();
        this.stcoBox = new Mp4StcoBox();
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.stsdBox.byteArrayLength() + this.sttsBox.byteArrayLength()
                + this.stscBox.byteArrayLength() + this.stszBox.byteArrayLength() + this.stcoBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.stsdBox.toByteArray())
                .putBytes(this.sttsBox.toByteArray())
                .putBytes(this.stscBox.toByteArray())
                .putBytes(this.stszBox.toByteArray())
                .putBytes(this.stcoBox.toByteArray())
                .getData();
    }
}
