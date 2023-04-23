package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import lombok.Data;

/**
 * bye
 *
 * @author xingshuang
 */
@Data
public final class RtcpBye implements IObjectByteArray {
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

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpBye fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpBye fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("解析RtcpBye时，字节数组长度不够");
        }
        RtcpBye res = new RtcpBye();
        res.header = RtcpSrHeader.fromBytes(data, offset);
        return res;
    }
}
