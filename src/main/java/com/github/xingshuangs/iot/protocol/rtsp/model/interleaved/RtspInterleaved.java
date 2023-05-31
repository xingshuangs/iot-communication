package com.github.xingshuangs.iot.protocol.rtsp.model.interleaved;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 交错帧
 *
 * @author xingshuang
 */
@Data
public class RtspInterleaved implements IObjectByteArray {

    public static final byte VERSION = (byte) 0x24;

    /**
     * 固定头，1个字节，表示Interleave Frame层的开始
     */
    private byte dollarSign = VERSION;

    /**
     * 通道Id，1个字节，协议类型，一般 0：Video RTP，1：Video RTCP，2: Audio RTP，3：Audio RTCP
     */
    private int channelId = 0;

    /**
     * 数据长度，2个字节, RTP包的大小
     */
    private int length = 0;

    /**
     * 剩下的就是负载
     */
    private byte[] payload = new byte[0];

    @Override
    public int byteArrayLength() {
        return 4 + payload.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.dollarSign)
                .putByte(this.channelId)
                .putInteger(this.length)
                .putBytes(this.payload)
                .getData();
    }

    public static List<RtspInterleaved> listFromBytes(final byte[] data) {
        List<RtspInterleaved> res = new ArrayList<>();
        int offset = 0;
        while (offset < data.length) {
            RtspInterleaved rtspInterleaved = fromBytes(data, offset);
            res.add(rtspInterleaved);
            offset += rtspInterleaved.byteArrayLength();
        }
        return res;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtspInterleaved fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtspInterleaved fromBytes(final byte[] data, final int offset) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("解析RtspInterleaved时，字节数组长度不够");
        }
        if (data[offset] != VERSION) {
            throw new IllegalArgumentException("报文版本不一致，第一个字节不是0x24");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtspInterleaved res = new RtspInterleaved();
        res.dollarSign = buff.getByte();
        res.channelId = buff.getByteToInt();
        res.length = buff.getUInt16();
        res.payload = buff.getBytes(res.length);
        return res;
    }
}
