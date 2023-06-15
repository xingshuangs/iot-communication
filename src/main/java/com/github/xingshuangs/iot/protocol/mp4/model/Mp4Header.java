package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;

import java.util.Collections;
import java.util.List;

/**
 * Mp4的头
 *
 * @author xingshuang
 */
public class Mp4Header implements IObjectByteArray {

    private final Mp4FtypBox ftypBox;

    private final Mp4MoovBox moovBox;

    public Mp4Header(Mp4TrackInfo trackInfo) {
        this.ftypBox = new Mp4FtypBox();
        this.moovBox = new Mp4MoovBox(Collections.singletonList(trackInfo));
    }

    public Mp4Header(List<Mp4TrackInfo> trackInfos) {
        this.ftypBox = new Mp4FtypBox();
        this.moovBox = new Mp4MoovBox(trackInfos);
    }

    @Override
    public int byteArrayLength() {
        return this.ftypBox.byteArrayLength() + this.moovBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putBytes(this.ftypBox.toByteArray())
                .putBytes(this.moovBox.toByteArray())
                .getData();
    }
}
