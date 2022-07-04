package com.github.xingshuangs.iot.protocol.modbus.model;


/**
 * 响应读输入寄存器
 *
 * @author xingshuang
 */
public class MbReadInputRegisterResponse extends MbPdu {

    /**
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * 输入寄存器值，N＝寄存器的数量，N*×2 个字节
     * 字节大小：N*×2 个字节
     */
    private byte[] register;
}
