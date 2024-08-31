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


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 1E帧访问路径
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McFrame1EAccessRoute extends McAccessRoute {

    /**
     * 可编程控制器编号，1个字节
     */
    private int pcNumber = 0xFF;

    public McFrame1EAccessRoute() {
    }

    public McFrame1EAccessRoute(int pcNumber) {
        this.pcNumber = pcNumber;
    }

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putByte(this.pcNumber)
                .getData();
    }

    public static McFrame1EAccessRoute createDefault() {
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        route.pcNumber = 0xFF;
        return route;
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return Mc4E3EFrameAccessRoute
     */
    public static McFrame1EAccessRoute fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return Mc4E3EFrameAccessRoute
     */
    public static McFrame1EAccessRoute fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset,true);
        McFrame1EAccessRoute res = new McFrame1EAccessRoute();
        res.pcNumber = buff.getByteToInt();
        return res;
    }
}
