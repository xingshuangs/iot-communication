package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * App
 *
 * @author xingshuang
 */
@Data
public final class RtcpApp extends RtcpBasePackage {

    /**
     * 同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。
     */
    private long sourceId;

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
                .putInteger(this.sourceId)
                .putBytes(des)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpApp fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpApp fromBytes(final byte[] data, final int offset) {
        if (data.length < 12) {
            throw new IndexOutOfBoundsException("解析RtcpBye时，字节数组长度不够");
        }
        RtcpApp res = new RtcpApp();
        res.header = RtcpHeader.fromBytes(data, offset);

        ByteReadBuff buff = new ByteReadBuff(data, offset + res.header.byteArrayLength());
        res.sourceId = buff.getUInt32();
        res.name = buff.getString(4);
        return res;
    }
}
