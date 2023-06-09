package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * mdat盒子
 *
 * @author xingshuang
 */
public class Mp4MdatBox extends Mp4Box {
    /**
     * 4字节数据包size
     */
    private int dataSize = 0;

    /**
     * 流媒体数据包，H264，采用avc1封装格式，即是去掉起始头后，就是nalu
     */
    private byte[] dataPackage = new byte[0];

    public Mp4MdatBox() {
    }

    public Mp4MdatBox(byte[] dataPackage) {
        this.mp4Type = EMp4Type.MDAT;
        this.dataPackage = dataPackage;
        this.dataSize = dataPackage.length;
    }

    @Override
    public int byteArrayLength() {
        return 12 + this.dataSize;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putInteger(this.dataSize)
                .putBytes(this.dataPackage)
                .getData();
    }
}
