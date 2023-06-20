package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;

/**
 * @author xingshuang
 */
public class Mp4SampleData {

    /**
     * 时间
     */
    private long timestamp = 0;

    /**
     * 数据，帧数据+长度
     */
    private byte[] data;

    /**
     * 数据大小，表示sample对应的数据帧的实际大小size=224251，帧数据字节+长度字节
     */
    private int size = 0;

    /**
     * 持续时间，一个sample的持续时间duration=3600
     */
    private int duration = 3600;

    /**
     * 时间, cts一般取值为0
     */
    private int cts = 0;

    /**
     * 标识位信息，表示sample-flags，只要碰到关键帧，DependsOn=2 ，其他等于0，非关键帧DependsOn=1，IsNonSync等于1，其他等于0
     * (sample.IsLeading << 2) | sample.DependsOn,
     * (sample.IsDependedOn << 6) | (sample.HasRedundancy << 4) | (0x00 << 1) | sample.IsNonSync,
     * sample.DegradPrio & 0xF0 << 8,
     * sample.DegradPrio & 0x0F,
     */
    private Mp4SampleFlag flags = new Mp4SampleFlag();

    public void setData(byte[] data) {
        this.size = 4 + data.length;
        ByteWriteBuff buff = new ByteWriteBuff(4 + data.length);
        buff.putInteger(data.length)
                .putBytes(data);
        this.data = buff.getData();
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public void setFlags(Mp4SampleFlag flags) {
        this.flags = flags;
    }

    public Mp4SampleFlag getFlags() {
        return flags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCts() {
        return cts;
    }

    public void setCts(int cts) {
        this.cts = cts;
    }
}
