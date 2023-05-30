package com.github.xingshuangs.iot.protocol.rtcp.service;


import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpSdesItemType;
import com.github.xingshuangs.iot.protocol.rtcp.model.*;
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
     * 同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。
     */
    private long sourceId;

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
    private int cumulativePacketLost = 0;

    /**
     * 序列号循环被使用次数
     */
    private int sequenceNumberCycles = 0;

    /**
     * 采样总时间
     */
    private long sampleTimestampSum = 0;

    /**
     * 上一次的RTP时间戳
     */
    private long lastRtpTimestamp = 0;

    /**
     * 上一次RTP的SSRC
     */
    private long lastRtpSsrc = 0;

    /**
     * 上一次RTCP发送SR的NTP时间
     */
    private long lastNtpTimeSenderReportReceived = 0;

    /**
     * 上一次接收到的SR的时间
     */
    private long lastTimeRtcpReportReceived = 0;

    public RtcpDataStatistics() {
        this.sourceId = System.currentTimeMillis();
    }

    /**
     * 处理RTP数据包
     *
     * @param rtp RTP数据包
     */
    public void processRtpPackage(RtpPackage rtp) {
        if (this.lastRtpSsrc > 0) {
            this.sampleTimestampSum += rtp.getHeader().getTimestamp() - this.lastRtpTimestamp;
            int delta = rtp.getHeader().getSequenceNumber() - this.highestSequenceNumber;
            if (delta > 1) {
                this.cumulativePacketLost += delta - 1;
                this.packetsLostSinceLastReset += delta - 1;
            }
            if (this.cumulativePacketLost > 0x7FFFFF) {
                this.cumulativePacketLost = 0x7FFFFF;
            }
            if (rtp.getHeader().getSequenceNumber() < this.highestSequenceNumber) {
                this.sequenceNumberCycles++;
            }
        }
        this.packetsReceivedSinceLastReset++;
        this.highestSequenceNumber = rtp.getHeader().getSequenceNumber();
        this.lastRtpSsrc = rtp.getHeader().getSsrc();
        this.lastRtpTimestamp = rtp.getHeader().getTimestamp();
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
        this.lastNtpTimeSenderReportReceived = rtcp.getSenderInfo().getMswTimestamp();
        this.lastTimeRtcpReportReceived = System.currentTimeMillis();
    }

    public RtcpReceiverReport createReceiverReport() {
        // 丢包率（8bit）:丢包率需要转换为0-255的占比；如20%丢包=20%*255=51
        int fractionLost = 0;
        if (this.packetsReceivedSinceLastReset != 0) {
            fractionLost = this.packetsLostSinceLastReset * 256 / this.packetsReceivedSinceLastReset;
        }
        // 期望接收的最大序列号，低16位存储期望最大序列号；高16为翻转次数统计
        long extHighestSequenceNumberReceived = ((long) this.sequenceNumberCycles << 16) | this.highestSequenceNumber;
        // 最后一次接收SR到发送的时延（32bit）:DLSR最后一次收到SR包后到发送中间的时延；
        long delaySinceLastTimeSenderReportReceived = 0;
        if (this.lastTimeRtcpReportReceived > 0) {
            delaySinceLastTimeSenderReportReceived = (System.currentTimeMillis() - this.lastTimeRtcpReportReceived) / 1000 * 65536;
        }
        RtcpReceiverReport receiverReport = new RtcpReceiverReport(this.sourceId);
        RtcpReportBlock reportBlock = new RtcpReportBlock();
        reportBlock.setSourceId(this.lastRtpSsrc);
        reportBlock.setFractionLost(fractionLost);
        reportBlock.setCumulativePacketLost(this.cumulativePacketLost);
        reportBlock.setExtHighestSequenceNumberReceived(extHighestSequenceNumberReceived);
        reportBlock.setJitter(0);
        reportBlock.setLastNtpTimeSenderReportReceived(this.lastNtpTimeSenderReportReceived);
        reportBlock.setDelaySinceLastTimeSenderReportReceived(delaySinceLastTimeSenderReportReceived);
        receiverReport.addRtcpReportBlock(reportBlock);
        return receiverReport;
    }

    public RtcpSdesReport createSdesReport() {
        RtcpSdesReport sdesReport = new RtcpSdesReport();
        RtcpSdesChunk chunk = new RtcpSdesChunk(this.sourceId);
        RtcpSdesItem item = new RtcpSdesItem();
        item.setType(ERtcpSdesItemType.CNAME);
        item.setText("iot-communication");
        item.setLength(17);
        chunk.addRtcpSdesItem(item);
        sdesReport.addRtcpSdesChunk(chunk);
        return sdesReport;
    }

    public RtcpBye createByte() {
        return new RtcpBye(this.sourceId);
    }
}
