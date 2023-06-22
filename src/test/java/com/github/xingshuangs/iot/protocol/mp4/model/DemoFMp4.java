package com.github.xingshuangs.iot.protocol.mp4.model;


import java.util.ArrayList;
import java.util.List;

/**
 * @author xingshuang
 */
public class DemoFMp4 {

    public static void main(String[] args) {
        List<Mp4SampleData> samples= new ArrayList<>();
        Mp4SampleData data = new Mp4SampleData();
        data.setTimestamp(System.currentTimeMillis());
        data.setData(new byte[5459]);
        data.setDuration(3600);
        data.setCts(0);
        Mp4SampleFlag flag = new Mp4SampleFlag();
        flag.setDependedOn(2);
        data.setFlags(flag);
        samples.add(data);

        Mp4TrackInfo trackInfo = new Mp4TrackInfo();
        trackInfo.setId(1);
        trackInfo.setType("video");
        trackInfo.setTimescale(90000);
        trackInfo.setDuration(90000);
        trackInfo.setWidth(1920);
        trackInfo.setHeight(1080);
        trackInfo.setSps(new byte[]{0x67, 0x64, 0x00, 0x2A, (byte) 0xAC, 0x2B, 0x50, 0x3C, 0x01, 0x13, (byte) 0xF2, (byte) 0xCD, (byte) 0xC0, 0x40, 0x40, 0x40, (byte) 0x80});
        trackInfo.setPps(new byte[]{0x68, (byte) 0xEE, 0x3C, (byte) 0xB0});
        trackInfo.setSampleData(samples);

        Mp4Header mp4FtypMoov = new Mp4Header(trackInfo);
        Mp4SampleData first = trackInfo.getSampleData().get(0);
        Mp4MoofBox mp4MoofBox = new Mp4MoofBox(1, first.getTimestamp(), trackInfo);
        Mp4MdatBox mp4MdatBox = new Mp4MdatBox(trackInfo.totalSampleData());
    }
}
