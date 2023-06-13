package com.github.xingshuangs.iot.protocol.mp4.model;


import lombok.Data;

/**
 * @author xingshuang
 */
@Data
public class Mp4SampleData {

    /**
     * 数据大小，表示sample对应的数据帧的实际大小size=224251
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
    private Mp4SampleFlag flags;
}
