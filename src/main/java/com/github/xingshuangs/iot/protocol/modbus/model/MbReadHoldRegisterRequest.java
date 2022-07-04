package com.github.xingshuangs.iot.protocol.modbus.model;


import lombok.Data;

/**
 * 请求读取线圈
 *
 * @author xingshuang
 */
@Data
public class MbReadHoldRegisterRequest extends MbPdu {

    /**
     * 地址 说是从0x0000 至 0xFFFF，但对应实际却只是0001-9999，对应0x0000-0x270F <br>
     * 字节大小：2个字节
     */
    private int address;

    /**
     * 寄存器数量，该功能码读取线圈的 1 至 2000 连续状态，1 至 2000（0x7D0）
     * 字节大小：2个字节
     */
    private int quantity;

}
