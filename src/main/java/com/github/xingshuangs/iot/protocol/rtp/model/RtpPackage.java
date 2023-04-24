package com.github.xingshuangs.iot.protocol.rtp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * RTP数据包
 *
 * @author xingshuang
 */
@Data
public class RtpPackage implements IObjectByteArray {

    /**
     * 头
     */
    private RtpHeader header;

    /**
     * 负载
     */
    private byte[] payload;

    /**
     * 需要忽略的长度
     */
    private int ignoreLength;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + this.payload.length + ignoreLength;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putBytes(this.header.toByteArray())
                .putBytes(this.payload)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtpPackage fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtpPackage fromBytes(final byte[] data, final int offset) {
        if (data.length < 12) {
            throw new IndexOutOfBoundsException("解析RtpPackage时，字节数组长度不够");
        }
        int index = offset;
        RtpPackage res = new RtpPackage();
        // 头
        res.header = RtpHeader.fromBytes(data, offset);
        index += res.header.byteArrayLength();
        // 最后一个填充字节标识了总共需要忽略多少个填充字节（包括自己）
        res.ignoreLength = res.header.isPadding() ? data[data.length - 1] & 0xFF : 0;
        // 负载
        ByteReadBuff buff = new ByteReadBuff(data, index);
        int payloadLength = data.length - index - res.ignoreLength;
        res.payload = buff.getBytes(payloadLength);
        return res;
    }
}
