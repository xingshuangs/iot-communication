package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.TimesUtil;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 发送者信息
 *
 * @author xingshuang
 */
@Data
public class RtcpSenderInfo implements IObjectByteArray {

    private long mswTimestamp;

    private long lswTimestamp;

    /**
     * NTP时间（64bit）：NTP时间包括两部分组成，高位32bit表示秒钟：从1970开始计算；低位表示剩余时间精度，一般按照1代表232皮秒来计算。
     */
    private LocalDateTime ntpTimestamp;

    /**
     * RTP时间戳（32bit）：与RTP时间戳计算方式一致，是根据采样率进行递增，由于与RTP时间戳一致同时又知道当前的NTP时间，因此可以用于音视频时间同步使用。
     */
    private LocalDateTime rtpTimestamp;

    /**
     * 发送包数量（32bit）：计算已经发送的包的数量
     * 从开始发送包到产生这个SR包这段时间里，发送者发送的RTP数据包的总数. SSRC改变时，这个域清零。
     */
    private long senderPacketCount;

    /**
     * 发送字节数（32bit）：计算已经发送的字节数量
     * 从开始发送包到产生这个SR包这段时间里，发送者发送的净荷数据的总字节数（不包括头部和填充）。发送者改变其SSRC时，这个域要清零。
     */
    private long senderOctetCount;

    @Override
    public int byteArrayLength() {
        return 20;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(20)
                .putInteger(this.mswTimestamp)
                .putInteger(this.lswTimestamp)
                .putInteger(TimesUtil.getNTPTotalSecond(this.rtpTimestamp))
                .putInteger(this.senderPacketCount)
                .putInteger(this.senderOctetCount)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpSenderInfo fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpSenderInfo fromBytes(final byte[] data, final int offset) {
        if (data.length < 20) {
            throw new IndexOutOfBoundsException("解析RtcpSenderInfo时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpSenderInfo res = new RtcpSenderInfo();
        res.mswTimestamp = buff.getUInt32();
        res.lswTimestamp = buff.getUInt32();
        res.ntpTimestamp = TimesUtil.getNTPDateTime(res.mswTimestamp);
        res.ntpTimestamp = res.ntpTimestamp.plusNanos(res.lswTimestamp * 232 / 1000);
        res.rtpTimestamp = TimesUtil.getNTPDateTime(buff.getUInt32());
        res.senderPacketCount = buff.getUInt32();
        res.senderOctetCount = buff.getUInt32();
        return res;
    }
}
