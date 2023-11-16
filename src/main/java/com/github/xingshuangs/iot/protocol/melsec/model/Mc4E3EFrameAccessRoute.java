package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 4E，3E帧访问路径
 *
 * @author xingshuang
 */
@Data
public class Mc4E3EFrameAccessRoute extends McAccessRoute {

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

    public static Mc4E3EFrameAccessRoute createDefault() {
        Mc4E3EFrameAccessRoute route = new Mc4E3EFrameAccessRoute();
        route.networkNumber = 0x00;
        route.pcNumber = 0xFF;
        route.requestDestModuleIoNumber = 0x03FF;
        route.requestDestModuleStationNumber = 0x00;
        return route;
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return Mc4E3EFrameAccessRoute
     */
    public static Mc4E3EFrameAccessRoute fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return Mc4E3EFrameAccessRoute
     */
    public static Mc4E3EFrameAccessRoute fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset,true);
        Mc4E3EFrameAccessRoute res = new Mc4E3EFrameAccessRoute();
        res.networkNumber = buff.getByteToInt();
        res.pcNumber = buff.getByteToInt();
        res.requestDestModuleIoNumber = buff.getUInt16();
        res.requestDestModuleStationNumber = buff.getByteToInt();
        return res;
    }
}
