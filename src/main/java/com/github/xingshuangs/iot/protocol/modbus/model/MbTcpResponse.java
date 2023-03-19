package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * TCP的modbus响应
 *
 * @author xingshuang
 */
@Data
public class MbTcpResponse implements IObjectByteArray {

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

    /**
     * 解析字节数组数据
     *
     * @param header   报文头
     * @param pduBytes pdu字节数组数据
     * @return MbTcpResponse
     */
    public static MbTcpResponse fromBytes(MbapHeader header, byte[] pduBytes) {
        MbTcpResponse response = new MbTcpResponse();
        response.header = header;
        response.pdu = MbPdu.fromBytes(pduBytes);
        return response;
    }
}
