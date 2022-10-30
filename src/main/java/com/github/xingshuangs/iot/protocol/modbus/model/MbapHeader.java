package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 报文头
 *
 * @author xingshuang
 */
@Data
public class MbapHeader implements IObjectByteArray {

    private static final AtomicInteger index = new AtomicInteger();

    public static final int BYTE_LENGTH = 7;

    /**
     * 事务元标识符<br>
     * 字节大小：2个字节
     * 字节序数：0-1
     */
    private int transactionId = 0;

    /**
     * 协议标识符<br>
     * 字节大小：2个字节
     * 字节序数：2-3
     */
    private int protocolId = 0;

    /**
     * 长度<br>
     * 字节大小：2个字节
     * 字节序数：4-5
     */
    private int length = 0;

    /**
     * 单元标识符<br>
     * 字节大小：1个字节
     * 字节序数：6
     */
    private int unitId = 0;

    public MbapHeader() {
        this.transactionId = getNewNumber();
    }

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putShort(this.transactionId)
                .putShort(this.protocolId)
                .putShort(this.length)
                .putByte(this.unitId)
                .getData();
    }

    /**
     * 获取新的pduNumber
     *
     * @return 编号
     */
    public static int getNewNumber() {
        int res = index.getAndIncrement();
        if (res >= 65536) {
            index.set(0);
            res = 0;
        }
        return res;
    }


    public static MbapHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbapHeader fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbapHeader res = new MbapHeader();
        res.transactionId = buff.getUInt16();
        res.protocolId = buff.getUInt16();
        res.length = buff.getUInt16();
        res.unitId = buff.getByteToInt();
        return res;
    }
}
