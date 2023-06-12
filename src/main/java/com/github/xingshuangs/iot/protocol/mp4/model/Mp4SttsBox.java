package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4SttsBox extends Mp4StcoBox {

    public Mp4SttsBox() {
        this.mp4Type = EMp4Type.STTS;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 0;
    }
}
