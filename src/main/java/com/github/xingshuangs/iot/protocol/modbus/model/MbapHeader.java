package com.github.xingshuangs.iot.protocol.modbus.model;


import lombok.Data;

/**
 * 报文头
 *
 * @author xingshuang
 */
@Data
public class MbapHeader {

    /**
     * 事务元标识符<br>
     * 字节大小：2个字节
     * 字节序数：0-1
     */
    private short transactionId;

    /**
     * 协议标识符<br>
     * 字节大小：2个字节
     * 字节序数：2-3
     */
    private int protocolId;

    /**
     * 长度<br>
     * 字节大小：2个字节
     * 字节序数：4-5
     */
    private int length;

    /**
     * 单元标识符<br>
     * 字节大小：1个字节
     * 字节序数：6
     */
    private short unitId;
}
