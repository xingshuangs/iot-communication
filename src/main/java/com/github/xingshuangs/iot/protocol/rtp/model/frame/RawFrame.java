package com.github.xingshuangs.iot.protocol.rtp.model.frame;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.rtp.enums.EFrameType;
import lombok.Getter;

/**
 * 帧的基础类
 *
 * @author xingshuang
 */
@Getter
public class RawFrame implements IObjectByteArray {

    /**
     * 帧类别
     */
    protected EFrameType frameType;

    /**
     * 时间戳
     */
    protected  long timestamp;

    /**
     * 帧内容
     */
    protected byte[] frameSegment = new byte[0];

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
