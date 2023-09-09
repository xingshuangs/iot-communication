package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 协议头
 *
 * @author xingshuang
 */
@Data
public class McHeader implements IObjectByteArray {

    /**
     * 副帧头，2字节
     */
    protected int subHeader;

    /**
     * 访问路径，存在多种访问路径
     */
    protected McAccessRoute accessRoute;

    /**
     * 数据长度，2字节，若请求就是请求数据长度，若响应就是响应数据长度
     */
    protected int dataLength;

    @Override
    public int byteArrayLength() {
        return 2+this.accessRoute.byteArrayLength()+2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(),true)
                .putShort(this.subHeader)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .getData();
    }
}
