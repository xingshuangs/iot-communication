package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * TCP的modbus请求
 *
 * @author xingshuang
 */
@Data
public class MbTcpRequest implements IByteArray {

    /**
     * 报文头， 报文头为 7 个字节长
     */
    private MbapHeader header;

    /**
     * 协议数据单元
     */
    private MbPdu pdu;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + this.pdu.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putBytes(this.header.toByteArray())
                .putBytes(this.pdu.toByteArray())
                .getData();
    }
}
