package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4Mp4aBox extends Mp4Box {

    /**
     * 6个字节
     */
    private final byte[] reserved1;

    /**
     * 2字节
     */
    private final int dataReferenceIndex;

    /**
     * 8个字节
     */
    private final byte[] reserved2;

    /**
     * 2字节通道数
     */
    private final int channelCount;

    /**
     * 2字节
     */
    private final int sampleSize;

    /**
     * 2字节
     */
    private final byte[] preDefined;

    /**
     * 2个字节
     */
    private final byte[] reserved3;

    /**
     * 2个字节
     */
    private final int audioSampleRate;

    /**
     * 2个字节
     */
    private final byte[] reserved4;

    private final Mp4EsdsBox esdsBox;

    public Mp4Mp4aBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MP4A;
        this.reserved1 = new byte[6];
        this.dataReferenceIndex = 1;
        this.reserved2 = new byte[8];
        this.channelCount = trackInfo.getChannelCount();
        this.sampleSize = 16;
        this.preDefined = new byte[2];
        this.reserved3 = new byte[2];
        this.audioSampleRate = trackInfo.getAudioSampleRate();
        this.reserved4 = new byte[2];

        this.esdsBox = new Mp4EsdsBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 36 + this.esdsBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.reserved1)
                .putShort(this.dataReferenceIndex)
                .putBytes(this.reserved2)
                .putShort(this.channelCount)
                .putShort(this.sampleSize)
                .putBytes(this.preDefined)
                .putBytes(this.reserved3)
                .putShort(this.audioSampleRate)
                .putBytes(this.reserved4)
                .putBytes(this.esdsBox.toByteArray())
                .getData();
    }
}
