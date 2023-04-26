package com.github.xingshuangs.iot.protocol.rtp.payload;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;

/**
 * @author xingshuang
 */
public class H264NaluStapSingle extends H264NaluSingle {

    private int size;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + 2 + this.payload.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putShort(this.size)
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
    public static H264NaluStapSingle fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluStapSingle fromBytes(final byte[] data, final int offset) {
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("解析H264NaluStapSingle时，字节数组长度不够");
        }
        int index = offset;
        H264NaluStapSingle res = new H264NaluStapSingle();
        res.size = ByteReadBuff.newInstance(data, index).getUInt16();
        index += 2;

        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        res.payload =  ByteReadBuff.newInstance(data, index).getBytes(res.size);
        return res;
    }
}
