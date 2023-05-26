package com.github.xingshuangs.iot.protocol.rtcp.service;


import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpReceiverReport;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpSdesReport;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpSenderReport;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import lombok.Data;

/**
 * RTP接收数据统计
 *
 * @author xingshuang
 */
@Data
public class RtcpDataStatistics {

    /**
     * 接收的最大序号
     */
    private int highestSequenceNumber = 0;

    /**
     * 从上一次重置之后的包接收数量
     */
    private int packetsReceivedSinceLastReset = 0;

    /**
     * 从上一次重置之后的丢包数量
     */
    private int packetsLostSinceLastReset = 0;

    /**
     * 累计丢包数量
     */
    private long cumulativePacketLost = 0;

    /**
     * 序列号循环被使用次数
     */
    private int sequenceNumberCycles = 0;

    /**
     * 采样总时间
     */
    private long sampleTimestampSum = 0;

    /**
     * 上一次的RTP数据包
     */
    private RtpPackage lastRtp;

    /**
     * 处理RTP数据包
     *
     * @param rtp RTP数据包
     */
    public void processRtpPackage(RtpPackage rtp) {
        this.highestSequenceNumber = rtp.getHeader().getSequenceNumber();
        this.packetsReceivedSinceLastReset++;

        if (this.lastRtp != null) {
            this.sampleTimestampSum += rtp.getHeader().getTimestamp() - this.lastRtp.getHeader().getTimestamp();
            int delta = rtp.getHeader().getSequenceNumber() - this.lastRtp.getHeader().getSequenceNumber();
            if (delta > 1) {
                this.cumulativePacketLost += delta - 1;
                this.packetsLostSinceLastReset += delta - 1;
            }
            if (rtp.getHeader().getSequenceNumber() < this.highestSequenceNumber) {
                this.sequenceNumberCycles++;
            }
        }
        this.lastRtp = rtp;
    }

    /**
     * 重置状态
     */
    public void resetState() {
        this.packetsLostSinceLastReset = 0;
        this.packetsReceivedSinceLastReset = 0;
    }

    /**
     * 处理RTCP数据包
     *
     * @param rtcp rtcp数据包
     */
    public void processRtcpPackage(RtcpSenderReport rtcp) {

    }

    public RtcpReceiverReport createReceiverReport() {
        return null;
    }

    public RtcpSdesReport createSdesReport() {
        return null;
    }
}
