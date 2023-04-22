package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源描述报告
 *
 * @author xingshuang
 */
@Data
public class RtcpSdesReport implements IObjectByteArray {

    private RtcpHeader header;

    private List<RtcpSdesChunk> sdesChunks = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        int length = 0;
        length += this.header != null ? this.header.byteArrayLength() : 0;
        for (RtcpSdesChunk chunk : this.sdesChunks) {
            length += chunk.byteArrayLength();
        }
        return length;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        if (this.header != null) {
            buff.putBytes(this.header.toByteArray());
        }
        for (RtcpSdesChunk chunk : this.sdesChunks) {
            buff.putBytes(chunk.toByteArray());
        }
        return buff.getData();
    }
}
