package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送者报告
 *
 * @author xingshuang
 */
@Data
public final class RtcpSenderReport implements IObjectByteArray {

    /**
     * 头
     */
    private RtcpSrHeader header;

    /**
     * 发送者信息
     */
    private RtcpSenderInfo senderInfo;

    /**
     * 报告数据块
     */
    private List<RtcpReportBlock> reportBlocks = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        int length = 0;
        length += this.header != null ? this.header.byteArrayLength() : 0;
        length += this.senderInfo != null ? this.senderInfo.byteArrayLength() : 0;
        for (RtcpReportBlock block : this.reportBlocks) {
            length += block.byteArrayLength();
        }
        return length;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        if (this.header != null) {
            buff.putBytes(this.header.toByteArray());
        }
        if (this.senderInfo != null) {
            buff.putBytes(this.senderInfo.toByteArray());
        }
        for (RtcpReportBlock block : this.reportBlocks) {
            buff.putBytes(block.toByteArray());
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpSenderReport fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpSenderReport fromBytes(final byte[] data, final int offset) {
        if (data.length < 28) {
            throw new IndexOutOfBoundsException("解析RtcpSenderReport时，字节数组长度不够");
        }
        int off = offset;
        RtcpSenderReport res = new RtcpSenderReport();
        res.header = RtcpSrHeader.fromBytes(data, off);
        off += res.header.byteArrayLength();
        res.senderInfo = RtcpSenderInfo.fromBytes(data, off);
        off += res.senderInfo.byteArrayLength();
        for (int i = 0; i < res.header.getReceptionCount(); i++) {
            RtcpReportBlock reportBlock = RtcpReportBlock.fromBytes(data, off);
            res.reportBlocks.add(reportBlock);
            off += reportBlock.byteArrayLength();
        }
        return res;
    }
}
