package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4AvcCBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 1字节profile
     */
    private final int profile;

    /**
     * 1字节compat
     */
    private final int profileCompat;

    /**
     * 1字节level
     */
    private final int level;

    /**
     * 1字节nalu长度头字节数, 此位标识nalu去掉起始位后，前面多少位用来表示nalu长度，此字节前五位默认为1,
     * 后三位值+1表示帧size占用的字节数，我们用4个字节来存储帧大小，所以这里为3，即为0xFC|0x03=0xFF
     */
    private final int lengthSizeMinusOne;

    /**
     * 1字节sps个数，前3位预留，默认位1，后五位表示sps个数，我们只放了一个sps，因此为E0|01=E1
     */
    private final int spsCount;

    /**
     * 2字节sps帧长度
     */
    private final int spsLength;

    /**
     * sps内容
     */
    private final byte[] sps;

    /**
     * 1字节pps个数
     */
    private final int ppsCount;

    /**
     * 2字节pps帧长度
     */
    private final int ppsLength;

    /**
     * pps内容
     */
    private final byte[] pps;

    public Mp4AvcCBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.AVCC;
        this.version = 1;
        this.profile = trackInfo.getSps()[1];
        this.profileCompat = trackInfo.getSps()[2];
        this.level = trackInfo.getSps()[3];
        this.lengthSizeMinusOne = 0xFF;
        this.spsCount = 0xE1;
        this.spsLength = trackInfo.getSps().length;
        this.sps = trackInfo.getSps();
        this.ppsCount = 1;
        this.ppsLength = trackInfo.getPps().length;
        this.pps = trackInfo.getPps();
    }

    @Override
    public int byteArrayLength() {
        return 19 + this.spsLength + this.ppsLength;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putByte(this.profile)
                .putByte(this.profileCompat)
                .putByte(this.level)
                .putByte(this.lengthSizeMinusOne)
                .putByte(this.spsCount)
                .putShort(this.spsLength)
                .putBytes(this.sps)
                .putByte(this.ppsCount)
                .putShort(this.ppsLength)
                .putBytes(this.pps)
                .getData();
    }
}
