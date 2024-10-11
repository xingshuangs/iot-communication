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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.Data;

/**
 * MC7 file content class.
 * mc7文件内容
 *
 * @author xingshuang
 */
@Data
public class Mc7File {

    /**
     * Source data.
     * 源数据
     */
    private byte[] data = new byte[0];

    /**
     * Block type.
     * 块类型
     */
    private EFileBlockType blockType;

    /**
     * Block number.
     * 块编号
     */
    private int blockNumber;

    /**
     * MC7 code length.
     * mc7代码长度
     */
    private int mC7CodeLength;

    /**
     * Byte array total length.
     * 字节数组总长度
     *
     * @return length
     */
    public int getLoadMemoryLength() {
        return this.data.length;
    }

    /**
     * Parses byte array and converts it to object.
     * 字节转换为对象
     *
     * @param data byte array
     * @return Mc7File
     */
    public static Mc7File fromBytes(final byte[] data) {
        if (data == null || data.length < 36) {
            throw new IllegalArgumentException("MC7, data length < 36");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        Mc7File res = new Mc7File();
        res.data = data;
        byte blockTypeByte = buff.getByte(5);
        res.blockType = EFileBlockType.from(HexUtil.toHexString(new byte[]{blockTypeByte}));
        res.blockNumber = buff.getUInt16(6);
        res.mC7CodeLength = buff.getUInt16(34);
        return res;
    }
}
