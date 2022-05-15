package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Getter;

/**
 * TPKT协议
 *
 * @author xingshuang
 */
@Getter
public class TPKT implements IByteArray {

    public static final int BYTE_LENGTH = 4;

    public static final int VERSION_OFFSET = 0;
    public static final int RESERVED_OFFSET = 1;
    public static final int LENGTH_OFFSET = 2;

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
        byte[] res = new byte[BYTE_LENGTH];
        byte[] lenBytes = ShortUtil.toByteArray(this.length);

        res[0] = this.version;
        res[1] = this.reserved;
        res[2] = lenBytes[0];
        res[3] = lenBytes[1];
        return res;
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
        TPKT tpkt = new TPKT();
        tpkt.version = data[VERSION_OFFSET];
        tpkt.reserved = data[RESERVED_OFFSET];
        tpkt.length = ShortUtil.toUInt16(data, 2);
        return tpkt;
    }
}
