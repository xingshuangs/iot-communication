package com.github.xingshuangs.iot.protocol.rtp.payload;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import lombok.Data;

/**
 * 单一Nalu模式
 *
 * @author xingshuang
 */
@Data
public class H264NaluSingle extends H264NaluBase {

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluSingle fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluSingle fromBytes(final byte[] data, final int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("解析H264NaluSingle时，字节数组长度不够");
        }
        H264NaluSingle res = new H264NaluSingle();
        int index = offset;
        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        ByteReadBuff buff = new ByteReadBuff(data, index);
        res.payload = buff.getBytes();
        return res;
    }
}
