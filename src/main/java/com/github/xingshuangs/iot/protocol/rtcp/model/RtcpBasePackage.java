package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;

/**
 * 基础包
 *
 * @author xingshuang
 */
public class RtcpBasePackage implements IObjectByteArray {
    /**
     * 头
     */
    protected RtcpHeader header;

    public RtcpHeader getHeader() {
        return header;
    }

    public void setHeader(RtcpHeader header) {
        this.header = header;
    }

    @Override
    public int byteArrayLength() {
        if (this.header == null) {
            return 0;
        }
        return this.header.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        if (this.header == null) {
            return new byte[0];
        }
        return this.header.toByteArray();
    }
}
