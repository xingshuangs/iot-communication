package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
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
    protected int length = 0x00;

    /**
     * PDU类型（CRConnect Request 连接请求）<br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    protected EPduType pduType = EPduType.CONNECT_REQUEST;

    @Override
    public int byteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(2)
                .putByte(this.length)
                .putByte(this.pduType.getCode())
                .getData();
    }
}
