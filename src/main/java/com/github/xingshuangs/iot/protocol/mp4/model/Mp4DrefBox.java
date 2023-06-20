package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * 每种音轨类型都有不同的媒体信息头（对应media handler-type）,其只包含版本和flags，fmp4中版本赋值为0，VMHD的flags=1，SMHD的flags=0
 *
 * @author xingshuang
 */
public class Mp4DrefBox extends Mp4Box {

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
    private final int entryCount;


    /**
     * 4字节
     */
    private final int entrySize;

    /**
     * 4字节
     */
    private final byte[] entryType;

    /**
     * 1字节
     */
    private final int entryVersion;

    /**
     * 3字节
     */
    private final byte[] entryFlag;

    public Mp4DrefBox() {
        this.mp4Type = EMp4Type.DREF;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 1;
        this.entrySize = 12;
        this.entryType = new byte[]{0x75, 0x72, 0x6c, 0x20};
        this.entryVersion = 0;
        this.entryFlag = new byte[]{0x00, 0x00, 0x01};
    }

    @Override
    public int byteArrayLength() {
        return 28;
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
                .putInteger(this.entrySize)
                .putBytes(this.entryType)
                .putByte(this.entryVersion)
                .putBytes(this.entryFlag)
                .getData();
    }
}
