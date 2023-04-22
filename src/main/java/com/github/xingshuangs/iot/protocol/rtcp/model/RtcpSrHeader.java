package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 有sourceId的头
 *
 * @author xingshuang
 */
@Data
public class RtcpSrHeader extends RtcpHeader {

    /**
     * 同步源（SSRC of sender）：32比特，SR包发送者的同步源标识符。与对应RTP包中的SSRC一样。
     */
    private long sourceId;

    @Override
    public int byteArrayLength() {
        return 8;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(8)
                .putBytes(super.toByteArray())
                .putInteger(this.sourceId)
                .getData();
    }
}
