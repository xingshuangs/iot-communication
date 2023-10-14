package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 请求头
 *
 * @author xingshuang
 */
@Data
public class McHeaderReq extends McHeader {

    /**
     * 监视定时器，2字节
     */
    private int monitoringTimer;

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
                .putShort(this.monitoringTimer)
                .getData();
    }
}
