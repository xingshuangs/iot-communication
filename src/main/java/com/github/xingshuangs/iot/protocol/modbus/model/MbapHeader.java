package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 报文头
 *
 * @author xingshuang
 */
@Data
public class MbapHeader implements IByteArray {

    /**
     * 事务元标识符<br>
     * 字节大小：2个字节
     * 字节序数：0-1
     */
    private int transactionId;

    /**
     * 协议标识符<br>
     * 字节大小：2个字节
     * 字节序数：2-3
     */
    private int protocolId;

    /**
     * 长度<br>
     * 字节大小：2个字节
     * 字节序数：4-5
     */
    private int length;

    /**
     * 单元标识符<br>
     * 字节大小：1个字节
     * 字节序数：6
     */
    private int unitId;

    @Override
    public int byteArrayLength() {
        return 7;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(7)
                .putShort(this.transactionId)
                .putShort(this.protocolId)
                .putShort(this.length)
                .putByte(this.unitId)
                .getData();
    }
}
