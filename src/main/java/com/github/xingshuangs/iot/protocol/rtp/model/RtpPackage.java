package com.github.xingshuangs.iot.protocol.rtp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;

/**
 * RTP数据包
 *
 * @author xingshuang
 */
public class RtpPackage implements IObjectByteArray {

    /**
     * 头
     */
    private RtpHeader header;

    /**
     * 负载
     */
    private byte[] payload;

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
