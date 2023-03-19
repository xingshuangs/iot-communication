package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * TCP的modbus请求
 *
 * @author xingshuang
 */
@Data
public class MbTcpRequest implements IObjectByteArray {

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
     * 自我数据校验
     */
    public void selfCheck() {
        if (this.header == null) {
            throw new ModbusCommException("header不能为null");
        }
        if (this.pdu == null) {
            throw new ModbusCommException("pdu不能为null");
        }
        this.header.setLength(this.pdu.byteArrayLength() + 1);
    }

    /**
     * 创建默认请求对象
     *
     * @return MbTcpRequest
     */
    public static MbTcpRequest createDefault() {
        MbTcpRequest request = new MbTcpRequest();
        request.header = new MbapHeader();
        return request;
    }
}
