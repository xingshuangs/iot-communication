package com.github.xingshuangs.iot.protocol.mp4.model;


import lombok.Data;

import java.util.List;

/**
 * 轨道信息
 *
 * @author xingshuang
 */
@Data
public class Mp4TrackInfo {

    private int id;

    private String type = "video";

    // region 视频
    private int timescale;

    private int duration;

    private int width;

    private int height;

    private int volume;

    private byte[] sps;

    private byte[] pps;
    // endregion

    // region 音频
    private int audioSampleRate;

    private int channelCount;

    private byte[] config;
    // endregion

    private List<Mp4SampleData> sampleData;
}
