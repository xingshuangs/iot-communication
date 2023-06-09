package com.github.xingshuangs.iot.protocol.mp4.model;


import lombok.Data;

/**
 * 轨道信息
 *
 * @author xingshuang
 */
@Data
public class Mp4TrackInfo {

    private int id;

    private String type = "video";

    private int duration;

    private int width;

    private int height;

    private int volume;

    private byte[] sps;

    private byte[] pps;
}
