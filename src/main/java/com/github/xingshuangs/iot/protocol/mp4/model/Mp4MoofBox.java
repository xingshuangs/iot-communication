package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * mdat盒子
 *
 * @author xingshuang
 */
public class Mp4MoofBox extends Mp4Box {

    /**
     * Movie Fragment header Box(mfhd)
     */
    private final Mp4MfhdBox mfhdBox;

    /**
     * Track Fragment Box(traf)
     */
    private final Mp4TrafBox trafBox;

    public Mp4MoofBox(int sequenceNumber, int baseMediaDecodeTime, Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MOOF;
        this.mfhdBox = new Mp4MfhdBox(sequenceNumber);
        this.trafBox = new Mp4TrafBox(trackInfo, baseMediaDecodeTime);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mfhdBox.byteArrayLength() + this.trafBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mfhdBox.toByteArray())
                .putBytes(this.trafBox.toByteArray())
                .getData();
    }
}
