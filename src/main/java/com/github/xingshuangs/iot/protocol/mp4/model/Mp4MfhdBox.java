package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Movie Fragment header Box(mfhd),包含分片序列信息，标识每个track的分片序列
 *
 * @author xingshuang
 */
public class Mp4MfhdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节媒体流序列号，每个trunk一个序列号，序列号从1开始递增，就是每个moof的序列号，相比前一个要加1
     */
    private final long sequenceNumber;

    public Mp4MfhdBox(long sequenceNumber) {
        this.mp4Type = EMp4Type.MFHD;
        this.version = 0;
        this.flags = new byte[3];
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int byteArrayLength() {
        return 16;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(this.sequenceNumber)
                .getData();
    }
}
