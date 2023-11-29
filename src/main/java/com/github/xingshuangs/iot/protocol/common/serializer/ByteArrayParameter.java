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

package com.github.xingshuangs.iot.protocol.common.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

/**
 * 字节数组参数
 *
 * @author xingshuang
 */
@Data
public class ByteArrayParameter {

    /**
     * 字节偏移量
     */
    private int byteOffset = 0;

    /**
     * 位偏移量
     */
    private int bitOffset = 0;

    /**
     * 数量，数量大于1的时候对应的数据必须使用list
     */
    private int count = 1;

    /**
     * 类型
     */
    private EDataType type = EDataType.BYTE;

    /**
     * 是否小端模式
     */
    private boolean littleEndian = false;

    /**
     * 具体的值
     */
    private Object value;

    public ByteArrayParameter() {
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.type = type;
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type, boolean littleEndian) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.type = type;
        this.littleEndian = littleEndian;
    }
}
