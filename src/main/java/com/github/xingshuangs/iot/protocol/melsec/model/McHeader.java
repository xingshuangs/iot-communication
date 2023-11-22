package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
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
    protected int subHeader = 0;

    /**
     * 访问路径，存在多种访问路径
     */
    protected McAccessRoute accessRoute;

    /**
     * 数据长度，2字节，请求数据长，指定从监视定时器到请求数据为止的数据长；响应数据长，
     * 存储从结束代码到响应数据(正常结束时)或出错信息(异常结束时)为止的数据长。
     */
    protected int dataLength = 0;

    @Override
    public int byteArrayLength() {
        return 2 + this.accessRoute.byteArrayLength() + 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.subHeader)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .getData();
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return McHeader
     */
    public static McHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return McHeaderAck
     */
    public static McHeader fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McHeader res = new McHeader();
        res.subHeader = buff.getUInt16();
        res.accessRoute = Mc4E3EFrameAccessRoute.fromBytes(buff.getBytes(5));
        res.dataLength = buff.getUInt16();
        return res;
    }
}
