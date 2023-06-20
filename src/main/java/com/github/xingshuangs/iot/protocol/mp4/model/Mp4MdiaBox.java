package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * mdia box包含 track 媒体数据信息的 container box,其子box主要包含视频流长度/创建时间/媒体播放过程/sample的偏移量/解码格式等
 *
 * @author xingshuang
 */
public class Mp4MdiaBox extends Mp4Box {

    private final Mp4MdhdBox mdhdBox;

    private final Mp4HdlrBox hdlrBox;

    private final Mp4MinfBox minfBox;

    public Mp4MdiaBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MDIA;
        this.mdhdBox = new Mp4MdhdBox(trackInfo);
        this.hdlrBox = new Mp4HdlrBox(trackInfo);
        this.minfBox = new Mp4MinfBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mdhdBox.byteArrayLength() + this.hdlrBox.byteArrayLength() + this.minfBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mdhdBox.toByteArray())
                .putBytes(this.hdlrBox.toByteArray())
                .putBytes(this.minfBox.toByteArray())
                .getData();
    }
}
