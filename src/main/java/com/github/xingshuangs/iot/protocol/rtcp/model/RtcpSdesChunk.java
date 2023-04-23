package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingshuang
 */
@Data
public class RtcpSdesChunk implements IObjectByteArray {

    /**
     * SSRC/CSRC
     */
    private long sourceId;

    /**
     * 所有项
     */
    private List<RtcpSdesItem> sdesItems = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        // item结束的时候会多一个字节
        int sum = this.sdesItems.stream().mapToInt(RtcpSdesItem::byteArrayLength).sum() + 1;
        // 保持偶数个字节
        sum = sum % 2 == 0 ? sum : sum + 1;
        return 4 + sum;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength())
                .putInteger(this.sourceId);
        for (RtcpSdesItem item : this.sdesItems) {
            buff.putBytes(item.toByteArray());
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpSdesChunk fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpSdesChunk fromBytes(final byte[] data, final int offset) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("解析RtcpSdesChunk时，字节数组长度不够");
        }
        int len = offset;
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpSdesChunk res = new RtcpSdesChunk();
        res.sourceId = buff.getUInt32();
        len += 4;

        while (data.length > len + 2) {
            RtcpSdesItem item = RtcpSdesItem.fromBytes(data, len);
            res.sdesItems.add(item);
            len += item.byteArrayLength();
        }
        return res;
    }
}
