package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Trac kExtends Box (trex) 是 mvex 的子box,用来给fMP4的sample设置默认值。
 *
 * @author xingshuang
 */
public class Mp4MvexBox extends Mp4Box {
    /**
     * 1字节，版本
     */
    private final List<Mp4Box> boxes;

    public Mp4MvexBox(List<Mp4TrackInfo> trackInfos) {
        this.mp4Type = EMp4Type.MVEX;
        this.boxes = trackInfos.stream().map(x -> new Mp4TrexBox(x.getId())).collect(Collectors.toList());
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray());
        for (Mp4Box box : boxes) {
            buff.putBytes(box.toByteArray());
        }
        return buff.getData();
    }
}
