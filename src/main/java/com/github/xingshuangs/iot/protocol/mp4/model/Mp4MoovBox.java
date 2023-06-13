package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Movie box包含本文件中所有媒体数据的宏观描述信息以及每路媒体轨道的具体信息,
 * fmp4格式需要紧跟在ftyp box之后，moov顶层box中没有具体信息，其信息都记录在其子box中
 *
 * @author xingshuang
 */
public class Mp4MoovBox extends Mp4Box {

    private final Mp4MvhdBox mvhdBox;

    private final List<Mp4TrakBox> trakBoxes;

    private final Mp4MvexBox mvexBox;

    public Mp4MoovBox(List<Mp4TrackInfo> trackInfos) {
        this.mp4Type = EMp4Type.MOOV;
        this.mvhdBox = new Mp4MvhdBox(trackInfos.get(0));
        this.trakBoxes = trackInfos.stream().map(Mp4TrakBox::new).collect(Collectors.toList());
        this.mvexBox = new Mp4MvexBox(trackInfos);
    }

    @Override
    public int byteArrayLength() {
        return 8 + this.mvhdBox.byteArrayLength()
                + this.trakBoxes.stream().mapToInt(Mp4TrakBox::byteArrayLength).sum()
                + this.mvexBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.mvhdBox.toByteArray());
        this.trakBoxes.forEach(x -> buff.putBytes(x.toByteArray()));
        buff.putBytes(this.mvexBox.toByteArray());
        return buff.getData();
    }
}
