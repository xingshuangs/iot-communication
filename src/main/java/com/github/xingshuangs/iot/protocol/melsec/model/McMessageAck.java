package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.common.buff.EByteBuffFormat;
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

    /**
     * 自我校验，主要核对数据长度
     */
    public void selfCheck() {
        this.header.dataLength = 2 + this.data.byteArrayLength();
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return McMessageAck
     */
    public static McMessageAck fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return McMessageAck
     */
    public static McMessageAck fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset,true);
        McMessageAck res = new McMessageAck();
        res.header = McHeaderAck.fromBytes(buff.getBytes(11));
        res.data = McAckData.fromBytes(buff.getBytes());
        return res;
    }
}
