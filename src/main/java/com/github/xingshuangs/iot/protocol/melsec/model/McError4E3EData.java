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
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import lombok.Data;

/**
 * 协议体数据：错误信息
 *
 * @author xingshuang
 */
@Data
public class McError4E3EData extends McData {

    /**
     * 访问路径，存在多种访问路径
     */
    private McAccessRoute accessRoute;

    /**
     * 指令，2个字节
     */
    private EMcCommand command;

    /**
     * 子指令，2个字节
     */
    private int subcommand = 0x0000;

    @Override
    public int byteArrayLength() {
        return 4 + this.accessRoute.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int length = 4 + this.accessRoute.byteArrayLength();
        return ByteWriteBuff.newInstance(length)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .getData();
    }

    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return McErrorInformationData
     */
    public static McError4E3EData fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return McErrorInformationData
     */
    public static McError4E3EData fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset, true);
        McError4E3EData res = new McError4E3EData();
        res.accessRoute = McFrame4E3EAccessRoute.fromBytes(buff.getBytes(5));
        res.command = EMcCommand.from(buff.getUInt16());
        res.subcommand = buff.getUInt16();
        return res;
    }
}
