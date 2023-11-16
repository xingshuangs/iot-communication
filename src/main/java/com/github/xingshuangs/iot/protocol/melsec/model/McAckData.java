package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import lombok.Data;

/**
 * 协议体数据：响应数据
 *
 * @author xingshuang
 */
@Data
public class McAckData extends McData {

    /**
     * 数据内容
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return this.data.length;
    }

    @Override
    public byte[] toByteArray() {
        return this.data;
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return McAckData
     */
    public static McAckData fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return McAckData
     */
    public static McAckData fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset,true);
        McAckData res = new McAckData();
        res.data = buff.getBytes();
        return res;
    }
}
