package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import lombok.Data;

import java.util.Arrays;

/**
 * COTP结构
 *
 * @author xingshuang
 */
@Data
public class COTP implements IByteArray {

    /**
     * 长度（但并不包含length这个字段）<br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private int length = 0x00;

    /**
     * PDU类型（CRConnect Request 连接请求）<br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private EPduType pduType = EPduType.CONNECT_REQUEST;

    @Override
    public int byteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[2];
        res[0] = (byte) (this.getLength() & 0xFF);
        res[1] = this.pduType.getCode();
        return res;
    }

    public static COTP fromBytes(byte[] data) {
        int length = data[0] & 0xFF;
        byte[] cotpBytes = Arrays.copyOfRange(data, 0, length + 1);
        if (cotpBytes[1] == EPduType.DT_DATA.getCode()) {
            return COTPData.fromBytes(cotpBytes);
        } else {

        }
        return null;
    }
}
