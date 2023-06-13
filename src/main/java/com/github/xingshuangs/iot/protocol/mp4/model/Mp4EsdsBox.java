package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4EsdsBox extends Mp4Box {

    /**
     * 1个字节
     */
    private final int version;

    /**
     * 3个字节
     */
    private final byte[] flags;

    /**
     * 1字节
     */
    private final int descriptorType1;

    /**
     * 1字节
     */
    private final int length1;

    /**
     * 2字节
     */
    private final int esId;

    /**
     * 1字节
     */
    private final int streamPriority;

    /**
     * 1字节
     */
    private final int descriptorType2;

    /**
     * 4字节
     */
    private final int length2;

    /**
     * 1字节
     */
    private final int codec;

    /**
     * 1字节
     */
    private final int streamType;

    /**
     * 3字节
     */
    private final byte[] bufferSize;

    /**
     * 4个字节
     */
    private final int maxBitrate;

    /**
     * 4个字节
     */
    private final int avgBitrate;

    /**
     * 1字节
     */
    private final int descriptorType3;

    /**
     * 1字节
     */
    private final int length3;

    private final byte[] config;

    private final byte[] lastData;

    public Mp4EsdsBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MP4A;
        this.version = 0;
        this.flags = new byte[3];
        this.descriptorType1 = 0x03;
        this.length1 = 0x17 + trackInfo.getConfig().length;
        this.esId = 1;
        this.streamPriority = 0;

        this.descriptorType2 = 0x04;
        this.length2 = 0x0f + trackInfo.getConfig().length;
        this.codec = 0x40;
        this.streamType = 0x15;
        this.bufferSize = new byte[3];
        this.maxBitrate = 0;
        this.avgBitrate = 0;

        this.descriptorType3 = 0x05;
        this.length3 = trackInfo.getConfig().length;
        this.config = trackInfo.getConfig();
        this.lastData = new byte[]{0x06, 0x01, 0x02};
    }

    @Override
    public int byteArrayLength() {
        return 37 + this.config.length;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putByte(this.descriptorType1)
                .putByte(this.length1)
                .putShort(this.esId)
                .putByte(this.streamPriority)
                .putByte(this.descriptorType2)
                .putByte(this.length2)
                .putByte(this.codec)
                .putByte(this.streamType)
                .putBytes(this.bufferSize)
                .putInteger(this.maxBitrate)
                .putInteger(this.avgBitrate)
                .putByte(this.descriptorType3)
                .putByte(this.length3)
                .putBytes(this.config)
                .putBytes(this.lastData)
                .getData();
    }
}
