/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
public class McFrame4E3EAccessRoute extends McAccessRoute {

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

    public McFrame4E3EAccessRoute() {
    }

    public McFrame4E3EAccessRoute(int networkNumber, int pcNumber, int requestDestModuleIoNumber, int requestDestModuleStationNumber) {
        this.networkNumber = networkNumber;
        this.pcNumber = pcNumber;
        this.requestDestModuleIoNumber = requestDestModuleIoNumber;
        this.requestDestModuleStationNumber = requestDestModuleStationNumber;
    }

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

    public static McFrame4E3EAccessRoute createDefault() {
        McFrame4E3EAccessRoute route = new McFrame4E3EAccessRoute();
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
    public static McFrame4E3EAccessRoute fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return Mc4E3EFrameAccessRoute
     */
    public static McFrame4E3EAccessRoute fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset,true);
        McFrame4E3EAccessRoute res = new McFrame4E3EAccessRoute();
        res.networkNumber = buff.getByteToInt();
        res.pcNumber = buff.getByteToInt();
        res.requestDestModuleIoNumber = buff.getUInt16();
        res.requestDestModuleStationNumber = buff.getByteToInt();
        return res;
    }
}
