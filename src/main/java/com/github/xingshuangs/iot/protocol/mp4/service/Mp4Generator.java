package com.github.xingshuangs.iot.protocol.mp4.service;


import com.github.xingshuangs.iot.protocol.mp4.model.FMp4Body;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4Header;
import com.github.xingshuangs.iot.protocol.mp4.model.Mp4TrackInfo;

/**
 * @author xingshuang
 */
public class Mp4Generator {

    public static Mp4Header createMp4Header(Mp4TrackInfo trackInfo) {
        return new Mp4Header(trackInfo);
    }

    public static FMp4Body createFMp4Body(Mp4TrackInfo trackInfo) {
        return new FMp4Body(trackInfo);
    }
}
