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
     * 副帧头，2字节，根据报文的类型定义设置的值。
     */
    protected int subHeader;

    /**
     * 访问路径，存在多种访问路径
     */
    protected McAccessRoute accessRoute;

    /**
     * 数据长度，2字节，请求数据长，指定从监视定时器到请求数据为止的数据长；响应数据长，
     * 存储从结束代码到响应数据(正常结束时)或出错信息(异常结束时)为止的数据长。
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
