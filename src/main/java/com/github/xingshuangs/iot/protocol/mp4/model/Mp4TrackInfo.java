package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
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

    private String codec = "avc1.64002a";

    // region 视频
    private int timescale;

    private int duration;

    private int width;

    private int height;

    private byte[] sps;

    private byte[] pps;
    // endregion

    // region 音频

    private int volume;

    private int audioSampleRate;

    private int channelCount;

    private byte[] config;
    // endregion

    private List<Mp4SampleData> sampleData = new ArrayList<>();

    public byte[] totalSampleData() {
        int sum = this.sampleData.stream().mapToInt(Mp4SampleData::getSize).sum();
        ByteWriteBuff buff = new ByteWriteBuff(sum);
        sampleData.forEach(x -> buff.putBytes(x.getData()));
        return buff.getData();
    }
}
