package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import lombok.Data;

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
    private byte length = 0x00;

    /**
     * PDU类型（CRConnect Request 连接请求）<br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private EPduType pduType = EPduType.CONNECT_REQUEST;

    @Override
    public int getByteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{this.length, this.pduType.getCode()};
    }
}
