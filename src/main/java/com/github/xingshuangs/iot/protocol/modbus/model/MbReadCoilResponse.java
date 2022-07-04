package com.github.xingshuangs.iot.protocol.modbus.model;


/**
 * 响应读取线圈
 *
 * @author xingshuang
 */
public class MbReadCoilResponse extends MbPdu {

    /**
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * 线圈状态N 或 N+1，N＝输出数量/8，如果余数不等于 0，那么N = N+1
     * 字节大小：N个字节
     */
    private byte[] coilStatus;
}
