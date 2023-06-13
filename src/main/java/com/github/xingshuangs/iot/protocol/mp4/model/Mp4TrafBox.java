package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Movie Fragment Box(moof),是 fmp4中数据追加的box,moof 和mdat是成对出现的,这个box是视频分片的描述信息，每个Fragment中包含moof
 * 和mdat，这样的结构符合渐进式播放需求。moof是一个顶级box,同时是一个容器box,下面紧跟一个mfhd,自身无内容。
 *
 * @author xingshuang
 */
public class Mp4TrafBox extends Mp4Box {

    /**
     * Track Fragment Header box(tfhd)
     */
    private final Mp4TfhdBox tfhdBox;

    /**
     * Track Fragment Base Media Decode Time Box(tfdt)
     */
    private final Mp4TfdtBox tfdtBox;

    /**
     * Track Fragment Run Box(trun)
     */
    private final Mp4TrunBox trunBox;

    /**
     * Independent and Disposable Samples Box(sdtp)
     */
    private final Mp4SdtpBox sdtpBox;

    public Mp4TrafBox(Mp4TrackInfo trackInfo, int baseMediaDecodeTime) {
        this.mp4Type = EMp4Type.TRAF;
        this.tfhdBox = new Mp4TfhdBox(trackInfo.getId());
        this.tfdtBox = new Mp4TfdtBox(baseMediaDecodeTime);
        this.trunBox = new Mp4TrunBox(trackInfo.getSampleData());
        this.sdtpBox = new Mp4SdtpBox(trackInfo.getSampleData());
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.tfhdBox.byteArrayLength() + this.tfdtBox.byteArrayLength()
                + this.trunBox.byteArrayLength() + this.sdtpBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.tfhdBox.toByteArray())
                .putBytes(this.tfdtBox.toByteArray())
                .putBytes(this.trunBox.toByteArray())
                .putBytes(this.sdtpBox.toByteArray())
                .getData();
    }
}
