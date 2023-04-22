package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * App
 *
 * @author xingshuang
 */
@Data
public class RtcpApp implements IObjectByteArray {

    /**
     * 头
     */
    private RtcpSrHeader header;

    /**
     * 名称
     */
    private String name;

    @Override
    public int byteArrayLength() {
        return 12;
    }

    @Override
    public byte[] toByteArray() {
        byte[] des = new byte[4];
        byte[] src = name.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(src, 0, des, 0, Math.min(des.length, src.length));
        return ByteWriteBuff.newInstance(12)
                .putBytes(this.header.toByteArray())
                .putBytes(des)
                .getData();
    }
}
