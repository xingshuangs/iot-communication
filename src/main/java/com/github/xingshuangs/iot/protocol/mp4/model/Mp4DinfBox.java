package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Data Reference Box 其包含dref box,fmp4给默认赋值即可
 *
 * @author xingshuang
 */
public class Mp4DinfBox extends Mp4Box {

    private final Mp4DrefBox drefBox;

    public Mp4DinfBox() {
        this.mp4Type = EMp4Type.DINF;
        this.drefBox = new Mp4DrefBox();
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.drefBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.drefBox.toByteArray())
                .getData();
    }
}
