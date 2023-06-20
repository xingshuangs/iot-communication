package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

import java.util.List;

/**
 * Independent and Disposable Samples Box(sdtp),主要是用来描述具体某个 sample 是否是 I 帧，是否是 leading frame 等相关属性值，
 * 主要用来作为当进行点播回放时的同步参考信息,这里简单赋值即可
 *
 * @author xingshuang
 */
public class Mp4SdtpBox extends Mp4Box {

    /**
     * 4字节预留
     */
    private final byte[] reserved;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    public Mp4SdtpBox(List<Mp4SampleData> samples) {
        this.mp4Type = EMp4Type.SDTP;
        this.reserved = new byte[4];
        this.flags = new byte[samples.size()];
        for (int i = 0; i < samples.size(); i++) {
            Mp4SampleFlag sampleFlag = samples.get(i).getFlags();
            this.flags[i] = (byte) ((sampleFlag.getDependedOn() << 4)
                    | (sampleFlag.getIsDependedOn() << 2)
                    | (sampleFlag.getHasRedundancy()));
        }
    }

    @Override
    public int byteArrayLength() {
        return 12 + this.flags.length;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.reserved)
                .putBytes(this.flags)
                .getData();
    }
}
