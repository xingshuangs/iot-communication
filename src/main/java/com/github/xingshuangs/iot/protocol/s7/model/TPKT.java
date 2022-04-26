package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.Getter;

/**
 * TPKT协议
 *
 * @author xingshuang
 */
@Getter
public class TPKT implements IByteArray {

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
     * 长度 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int length = 0x0000;

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int byteArrayLength() {
        return 4;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[4];
        byte[] lenBytes = ShortUtil.toByteArray((short) this.length);

        res[0] = this.version;
        res[1] = this.reserved;
        res[2] = lenBytes[0];
        res[3] = lenBytes[1];
        return res;
    }
}
