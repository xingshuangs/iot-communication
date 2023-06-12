package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4StcoBox extends Mp4Box {
    /**
     * 1字节，版本
     */
    protected int version;

    /**
     * 3字节为flags
     */
    protected byte[] flags;

    /**
     * 4字节
     */
    protected int entryCount;

    public Mp4StcoBox() {
        this.mp4Type = EMp4Type.STCO;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 0;
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
                .putInteger(this.entryCount)
                .getData();
    }
}
