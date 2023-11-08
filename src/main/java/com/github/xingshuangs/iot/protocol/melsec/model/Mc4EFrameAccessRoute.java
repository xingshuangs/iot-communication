package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 4C帧访问路径
 *
 * @author xingshuang
 */
@Data
public class Mc4EFrameAccessRoute extends McAccessRoute {

    /**
     * 网络编号，1个字节
     */
    private int networkNumber = 0x00;

    /**
     * 可编程控制器编号，1个字节
     */
    private int pcNumber = 0xFF;

    /**
     * 请求目标模块I/O编号
     */
    private int requestDestModuleIoNumber = 0x03FF;

    /**
     * 请求目标模块站号，1个字节
     */
    private int requestDestModuleStationNumber = 0x00;

    @Override
    public int byteArrayLength() {
        return 5;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putByte(this.networkNumber)
                .putByte(this.pcNumber)
                .putShort(this.requestDestModuleIoNumber)
                .putByte(this.requestDestModuleStationNumber)
                .getData();
    }
}
