package com.github.xingshuangs.iot.protocol.modbus.model;


import lombok.Data;

/**
 * TCP的modbus请求
 *
 * @author xingshuang
 */
@Data
public class MbTcpRequest {

    /**
     * 报文头， 报文头为 7 个字节长
     */
    private MbapHeader header;

    /**
     * 协议数据单元
     */
    private MbPdu pdu;
}
