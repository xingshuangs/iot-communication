package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * stsd-avc1-btrt bitrate box描述码率信息，其包含最大码率，平均码率等信息
 *
 * @author xingshuang
 */
public class Mp4BtrtBox extends Mp4Box {

    /**
     * 4字节，缓存大小
     */
    private final int bufferSizeDB;

    /**
     * 4字节为最大码率
     */
    private final int maxBitRate;

    /**
     * 4字节为平均码率
     */
    private final int avgBitRate;

    public Mp4BtrtBox() {
        this.mp4Type = EMp4Type.BTRT;
        this.bufferSizeDB = 1_875_072;
        this.maxBitRate = 3_000_000;
        this.avgBitRate = 3_000_000;
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
                .putInteger(this.bufferSizeDB)
                .putInteger(this.maxBitRate)
                .putInteger(this.avgBitRate)
                .getData();
    }
}
