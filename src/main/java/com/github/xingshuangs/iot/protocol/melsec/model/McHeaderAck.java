package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 响应头
 *
 * @author xingshuang
 */
@Data
public class McHeaderAck extends McHeader {

    /**
     * 结束代码，2字节
     */
    private int endCode;

    @Override
    public int byteArrayLength() {
        return 2 + this.accessRoute.byteArrayLength() + 2 + 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.subHeader)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .putShort(this.endCode)
                .getData();
    }
}
