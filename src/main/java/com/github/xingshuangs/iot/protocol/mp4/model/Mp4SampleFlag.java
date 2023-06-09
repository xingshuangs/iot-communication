package com.github.xingshuangs.iot.protocol.mp4.model;


import lombok.Data;

/**
 * 采样的相关属性
 * 这里以单个分片单个sample的模式，只要碰到关键帧，DependsOn=2 ，其他等于0，非关键帧DependsOn=1，IsNonSync等于1，其他等于0
 *
 * @author xingshuang
 */
@Data
public class Mp4SampleFlag {

    private int isLeading = 0;

    private int isDependedOn = 0;

    private int hasRedundancy = 0;

    private int degradPrio = 0;

    private int dependedOn = 0;

    private int isNonSync = 0;

    private int paddingValue = 0;
}
