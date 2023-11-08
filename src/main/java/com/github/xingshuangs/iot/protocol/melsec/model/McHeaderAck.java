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
     * 存储指令处理结果。
     * 正常结束时将存储0。
     * 异常结束时将存储访问目标的出错代码。
     * 出错代码表示发生的出错内容。
     * 同时发生了多个出错的情况下，将返回最初检测的出错代码。
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
