package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 响应消息
 *
 * @author xingshuang
 */
@Data
public class McMessageAck implements IObjectByteArray {
    /**
     * 响应头
     */
    private McHeaderAck header;

    /**
     * 响应数据
     */
    private McData data;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + this.data.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putBytes(this.header.toByteArray())
                .putBytes(this.data.toByteArray())
                .getData();
    }
}
