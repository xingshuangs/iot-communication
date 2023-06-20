package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Size Boxes (stbl-stsz),指定了每个sample的size,针对fmp4这里无需赋值
 *
 * @author xingshuang
 */
public class Mp4StszBox extends Mp4Box {
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
    private final int sampleSize;

    /**
     * 4字节
     */
    private final int sampleCount;

    public Mp4StszBox() {
        this.mp4Type = EMp4Type.STSZ;
        this.version = 0;
        this.flags = new byte[3];
        this.sampleSize = 0;
        this.sampleCount = 0;
    }

    @Override
    public int byteArrayLength() {
        return 20;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.sampleSize)
                .putInteger(this.sampleCount)
                .getData();
    }
}
