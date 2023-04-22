package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
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
        int sum = this.sdesItems.stream().mapToInt(RtcpSdesItem::getLength).sum() + 1;
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
}
