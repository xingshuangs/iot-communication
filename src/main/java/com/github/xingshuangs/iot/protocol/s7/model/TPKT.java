package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Getter;

/**
 * TPKT协议
 *
 * @author xingshuang
 */
@Getter
public class TPKT implements IObjectByteArray {

    public static final int BYTE_LENGTH = 4;

    /**
     * 版本号，常量0x03 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private byte version = 0x03;

    /**
     * 预留，默认值0x00 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private byte reserved = 0x00;

    /**
     * 长度，包括后面负载payload+版本号+预留+长度 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int length = 0x0000;

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.version)
                .putByte(this.reserved)
                .putShort(this.length)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return TPKT
     */
    public static TPKT fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new IndexOutOfBoundsException(String.format("TPKT转换过程中，字节数据长度小于%d", BYTE_LENGTH));
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        TPKT tpkt = new TPKT();
        tpkt.version = buff.getByte();
        tpkt.reserved = buff.getByte();
        tpkt.length = buff.getUInt16();
        return tpkt;
    }
}
