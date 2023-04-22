package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import lombok.Data;

/**
 * bye
 *
 * @author xingshuang
 */
@Data
public class RtcpBye implements IObjectByteArray {
    /**
     * 头
     */
    private RtcpSrHeader header;

    @Override
    public int byteArrayLength() {
        return this.header.length;
    }

    @Override
    public byte[] toByteArray() {
        return this.header.toByteArray();
    }
}
