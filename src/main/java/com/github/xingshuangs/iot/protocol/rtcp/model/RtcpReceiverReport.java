package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收者报告
 *
 * @author xingshuang
 */
@Data
public final class RtcpReceiverReport extends RtcpBasePackage {

    /**
     * 同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。
     */
    private long sourceId;

    /**
     * 报告数据块
     */
    private List<RtcpReportBlock> reportBlocks = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        int length = 0;
        length += this.header != null ? this.header.byteArrayLength() : 0;
        length += 4;
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
        buff.putInteger(this.sourceId);
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
    public static RtcpReceiverReport fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpReceiverReport fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("解析RtcpReceiverReport时，字节数组长度不够");
        }
        int off = offset;
        RtcpReceiverReport res = new RtcpReceiverReport();
        res.header = RtcpHeader.fromBytes(data, off);
        off += res.header.byteArrayLength();

        res.sourceId = ByteReadBuff.newInstance(data, off).getUInt32();
        off += 4;

        for (int i = 0; i < res.header.getReceptionCount(); i++) {
            RtcpReportBlock reportBlock = RtcpReportBlock.fromBytes(data, off);
            res.reportBlocks.add(reportBlock);
            off += reportBlock.byteArrayLength();
        }
        return res;
    }
}
