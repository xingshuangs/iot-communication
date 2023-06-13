package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

import java.util.List;

/**
 * Track Fragment Run Box(trun),记录moof中有关sample的相关信息，如每个sample的size,duration,offset等信息，
 *
 * @author xingshuang
 */
public class Mp4TrunBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节 表示和moof配套的mdat中实际数据位置距离moof开头有多少偏移，其等于整个moof的长度+mdat的头长度
     */
    private final int offset;

    /**
     * 采样数据
     */
    private final List<Mp4SampleData> samples;

    public Mp4TrunBox(List<Mp4SampleData> samples) {
        this.mp4Type = EMp4Type.TRUN;
        this.version = 0;
        // 表示每个sample都是用字节的参数
        this.flags = new byte[]{0x00, 0x0F, 0x01};
        this.offset = 8 +  // moof header
                16 + // mfhd
                8 +  // traf header
                16 + // tfhd
                16 + // tfdt
                20 + 16 * samples.size() + // trun
                12 + samples.size() + // sdtp
                8;  // mdat header;
        this.samples = samples;
    }

    @Override
    public int byteArrayLength() {
        return 20 + 16 * this.samples.size();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        // 表示sample数量
        int length = this.samples.size();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(length)
                .putInteger(this.offset);
        for (Mp4SampleData sample : this.samples) {
            Mp4SampleFlag flag = sample.getFlags();
            buff.putInteger(sample.getDuration())
                    .putInteger(sample.getSize())
                    .putByte((flag.getIsLeading() << 2) | flag.getDependedOn())
                    .putByte((flag.getIsDependedOn() << 6)
                            | (flag.getHasRedundancy() << 4)
                            | (flag.getPaddingValue() << 1)
                            | flag.getIsNonSync())
                    .putByte((flag.getDegradPrio() & (byte) 0xF0) << 8)
                    .putByte(flag.getDegradPrio() & (byte) 0x0F)
                    .putInteger(sample.getCts());
        }
        return buff.getData();
    }
}
