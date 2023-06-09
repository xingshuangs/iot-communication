package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * 每种音轨类型都有不同的媒体信息头（对应media handler-type）,其只包含版本和flags，fmp4中版本赋值为0，VMHD的flags=1，SMHD的flags=0
 *
 * @author xingshuang
 */
public class Mp4VmhdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 2字节 graphicsmode
     */
    private final byte[] graphicsmode;

    /**
     * 6字节 opcolor
     */
    private final byte[] opcolor;

    public Mp4VmhdBox() {
        this.mp4Type = EMp4Type.VMHD;
        this.version = 0;
        this.flags = new byte[]{0x00, 0x00, 0x01};
        this.graphicsmode = new byte[2];
        this.opcolor = new byte[6];
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
                .putBytes(this.graphicsmode)
                .putBytes(this.opcolor)
                .getData();
    }
}
