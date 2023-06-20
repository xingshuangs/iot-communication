package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Media Information Box，解释 track 媒体数据的 handler-specific 信息。minf 同样是个 container box，Media Information Box，
 * 解释 track 媒体数据的 handler-specific 信息。minf 同样是个 container box，其内部需要关注的内容是 stbl，这也是 moov 中最复杂的部分。
 *
 * @author xingshuang
 */
public class Mp4MinfBox extends Mp4Box {

    private final Mp4Box mhdBox;

    private final Mp4DinfBox dinfBox;

    private final Mp4StblBox stblBox;

    public Mp4MinfBox(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.MINF;
        this.mhdBox = trackInfo.getType().equals("video") ? new Mp4VmhdBox() : new Mp4SmhdBox();
        this.dinfBox = new Mp4DinfBox();
        this.stblBox = new Mp4StblBox(trackInfo);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mhdBox.byteArrayLength() + this.dinfBox.byteArrayLength() + this.stblBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mhdBox.toByteArray())
                .putBytes(this.dinfBox.toByteArray())
                .putBytes(this.stblBox.toByteArray())
                .getData();
    }
}
