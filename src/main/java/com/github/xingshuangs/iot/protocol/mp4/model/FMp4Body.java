package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;

/**
 * FMp4的数据体
 *
 * @author xingshuang
 */
public class FMp4Body implements IObjectByteArray {

    private final Mp4MoofBox moofBox;

    private final Mp4MdatBox mdatBox;

    public FMp4Body(Mp4TrackInfo trackInfo) {
        this.moofBox = new Mp4MoofBox(trackInfo);
        this.mdatBox = new Mp4MdatBox(trackInfo.totalSampleData());
    }

    @Override
    public int byteArrayLength() {
        return this.moofBox.byteArrayLength() + this.mdatBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putBytes(this.moofBox.toByteArray())
                .putBytes(this.mdatBox.toByteArray())
                .getData();
    }
}
