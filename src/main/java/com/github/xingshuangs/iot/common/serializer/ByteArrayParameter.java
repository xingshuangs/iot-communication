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

package com.github.xingshuangs.iot.common.serializer;


import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.common.enums.EDataType;
import lombok.Data;

/**
 * Byte array parameter
 * (字节数组参数)
 *
 * @author xingshuang
 */
@Data
public class ByteArrayParameter {

    /**
     * Byte offset.
     * (字节偏移量)
     */
    protected int byteOffset = 0;

    /**
     * Bit offset.
     * (位偏移量)
     */
    protected int bitOffset = 0;

    /**
     * Count, if count bigger than 1, then must use list type.
     * (数量，数量大于1的时候对应的数据必须使用list)
     */
    protected int count = 1;

    /**
     * Data type.
     * (类型)
     */
    protected EDataType type = EDataType.BYTE;

    /**
     * Is little endian.
     * (是否小端模式)
     */
    protected boolean littleEndian = false;

    /**
     * 4 - or 8-byte encoding format.
     * (4字节和8字节的编码格式)
     */
    protected EByteBuffFormat format = EByteBuffFormat.DC_BA;

    /**
     * Actual Value
     * (具体的值)
     */
    protected Object value;

    public ByteArrayParameter() {
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type) {
        this(byteOffset, bitOffset, count, type, false, EByteBuffFormat.DC_BA);
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type, boolean littleEndian) {
        this(byteOffset, bitOffset, count, type, littleEndian, EByteBuffFormat.DC_BA);
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type, boolean littleEndian, EByteBuffFormat format) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.type = type;
        this.littleEndian = littleEndian;
        this.format = format;
    }
}
