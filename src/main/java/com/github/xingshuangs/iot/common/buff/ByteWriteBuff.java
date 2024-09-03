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

package com.github.xingshuangs.iot.common.buff;


import com.github.xingshuangs.iot.utils.*;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Write byte array buffer class.
 * 写字节缓存
 *
 * @author xingshuang
 */
@Getter
public class ByteWriteBuff {

    /**
     * 4 - or 8-byte encoding format.
     * (4字节或8字节的编码格式)
     */
    private final EByteBuffFormat format;

    /**
     * Data source.
     * (数据)
     */
    private final byte[] data;

    /**
     * Current offset.
     * (偏移量)
     */
    private int offset = 0;

    /**
     * Is little endian. The default is not, big endian mode.
     * (是否为小端模式，默认不是，为大端模式)
     */
    private final boolean littleEndian;

    /**
     * Construct
     * (构造方法)
     *
     * @param capacity capacity
     */
    public ByteWriteBuff(int capacity) {
        this(capacity, false, EByteBuffFormat.DC_BA);
    }

    public ByteWriteBuff(int capacity, boolean littleEndian) {
        this(capacity, littleEndian, EByteBuffFormat.DC_BA);
    }

    public ByteWriteBuff(int capacity, EByteBuffFormat format) {
        this(capacity, false, format);
    }

    public ByteWriteBuff(int capacity, boolean littleEndian, EByteBuffFormat format) {
        this.littleEndian = littleEndian;
        this.data = new byte[capacity];
        this.format = format;
    }

    public static ByteWriteBuff newInstance(int capacity) {
        return new ByteWriteBuff(capacity);
    }

    public static ByteWriteBuff newInstance(int capacity, boolean littleEndian) {
        return new ByteWriteBuff(capacity, littleEndian, EByteBuffFormat.DC_BA);
    }

    public static ByteWriteBuff newInstance(int capacity, EByteBuffFormat format) {
        return new ByteWriteBuff(capacity, false, format);
    }

    public static ByteWriteBuff newInstance(int capacity, boolean littleEndian, EByteBuffFormat format) {
        return new ByteWriteBuff(capacity, littleEndian, format);
    }

    /**
     * Get a byte by byte index.
     * (获取指定索引的字节)
     *
     * @param index byte index
     * @return byte data
     */
    public byte getByte(int index) {
        if (index > data.length - 1) {
            throw new IndexOutOfBoundsException("index");
        }
        return this.data[index];
    }

    /**
     * Check condition.
     * (校验条件)
     *
     * @param desIndex     destination index
     * @param targetLength target length
     */
    private void checkCondition(int desIndex, int targetLength) {
        if (desIndex + targetLength > data.length) {
            // 超过字节数组最大容量
            throw new IllegalArgumentException("Exceeds the maximum capacity of the byte array");
        }
    }

    /**
     * Add a byte data.
     * (添加字节数据)
     *
     * @param src a byte data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putByte(byte src) {
        return this.putByte(src, this.offset);
    }

    /**
     * Add a byte data by destination index.
     *
     * @param src      a byte data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putByte(byte src, int desIndex) {
        this.checkCondition(desIndex, 1);
        this.data[desIndex] = src;
        if (this.offset == desIndex) {
            this.offset++;
        }
        return this;
    }

    /**
     * Add a int data.
     * (添加int类型单字节数据)
     *
     * @param src a byte data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putByte(int src) {
        return this.putByte(ByteUtil.toByte(src));
    }

    /**
     * Add byte array.
     * (添加字节数组数据)
     *
     * @param src a byte data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putBytes(byte[] src) {
        return this.putBytes(src, 0, this.offset);
    }

    /**
     * Add byte array by source index.
     * (添加字节数组数据)
     *
     * @param src      source data
     * @param srcIndex source data index
     * @return 对象本身
     */
    public ByteWriteBuff putBytes(byte[] src, int srcIndex) {
        return this.putBytes(src, srcIndex, this.offset);
    }

    /**
     * Add byte array by source index and destination index. If destination index equal current offset,
     * then current offset +1, otherwise leave as is.
     * (添加字节数组数据，当desIndex==this.offset时，才将this.offset进行偏移，否则保持不变)
     *
     * @param src      data source
     * @param srcIndex source data index
     * @param desIndex destination data index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putBytes(byte[] src, int srcIndex, int desIndex) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        this.checkCondition(desIndex, src.length - srcIndex);
        System.arraycopy(src, srcIndex, this.data, desIndex, src.length - srcIndex);
        if (desIndex == this.offset) {
            this.offset += src.length;
        }
        return this;
    }

    /**
     * Add a short data.
     * (添加 short 类型数据)
     *
     * @param src a short data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(short src) {
        return this.putShort(src, this.offset, this.littleEndian);
    }

    /**
     * Add a short data by destination index.
     * (添加 short 类型数据)
     *
     * @param src      a short data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(short src, int desIndex) {
        return this.putShort(src, desIndex, this.littleEndian);
    }

    /**
     * Add a short data from int data.
     * (添加 short 类型数据)
     *
     * @param src a int data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(int src) {
        return this.putBytes(ShortUtil.toByteArray(src, this.littleEndian));
    }

    /**
     * Add a short data from int data by destination index.
     * (添加short数据)
     *
     * @param src      a int data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(int src, int desIndex) {
        return this.putBytes(ShortUtil.toByteArray(src, this.littleEndian), 0, desIndex);
    }

    /**
     * Add a int data.
     * (添加integer数据)
     *
     * @param src a int data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(int src) {
        return this.putInteger(src, this.offset, this.littleEndian);
    }

    /**
     * Add a int data by destination index.
     * 添加Integer数据
     *
     * @param src      a int data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(int src, int desIndex) {
        return this.putInteger(src, desIndex, this.littleEndian);
    }

    /**
     * Add a int data from a long data.
     * (添加integer数据)
     *
     * @param src a long data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(long src) {
        return this.putInteger(src, this.offset, this.littleEndian);
    }

    /**
     * Add a int data from a long data by destination index.
     * (添加Integer数据)
     *
     * @param src      a long data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(long src, int desIndex) {
        return this.putInteger(src, desIndex, this.littleEndian);
    }

    /**
     * Add a long data.
     * (添加long数据)
     *
     * @param src a long data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putLong(long src) {
        return this.putLong(src, this.offset, this.littleEndian);
    }

    /**
     * Add a long data by destination index.
     * (添加long数据)
     *
     * @param src      a long data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putLong(long src, int desIndex) {
        return this.putLong(src, desIndex, this.littleEndian);
    }

    /**
     * Add a float data.
     * (添加float数据)
     *
     * @param src a float data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putFloat(float src) {
        return this.putFloat(src, this.offset, this.littleEndian);
    }

    /**
     * Add a float data by destination index.
     * (添加float数据)
     *
     * @param src      a float data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putFloat(float src, int desIndex) {
        return this.putFloat(src, desIndex, this.littleEndian);
    }

    /**
     * Add a double data.
     * (添加double数据)
     *
     * @param src a double data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putDouble(double src) {
        return this.putDouble(src, this.offset, this.littleEndian);
    }

    /**
     * Add a double data by destination index.
     * (添加double数据)
     *
     * @param src      a double data
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putDouble(double src, int desIndex) {
        return this.putDouble(src, desIndex, this.littleEndian);
    }

    /**
     * Add a string data.
     * (添加string数据)
     *
     * @param src a string data
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putString(String src) {
        return this.putString(src, StandardCharsets.US_ASCII, this.offset);
    }

    /**
     * Add a string data by charsets.
     * (添加string数据)
     *
     * @param src      a string data
     * @param charsets target charsets
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putString(String src, Charset charsets) {
        return this.putString(src, charsets, this.offset);
    }

    /**
     * Add a short data by destination index and endian.
     * (添加short数据)
     *
     * @param src          a short data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(short src, int desIndex, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian), 0, desIndex);
    }

    /**
     * Add a short data from a int data by destination index and endian.
     * (添加short数据)
     *
     * @param src          a int data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putShort(int src, int desIndex, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian), 0, desIndex);
    }

    /**
     * Add a int data by destination index and endian.
     * (添加integer数据)
     *
     * @param src          a int data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(int src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(IntegerUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a int data from a long data by destination index and endian.
     * (添加integer数据)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(long src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(IntegerUtil.toByteArray((int) src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a int data from a long data by destination index, endian and format.
     * (添加integer数据，针对特殊EByteBuffFormat的处理)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @param format       EByteBuffFormat format
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putInteger(long src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn4Bytes(IntegerUtil.toByteArray((int) src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a long data by destination index and endian.
     * (添加long数据)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putLong(long src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn8Bytes(LongUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a long data by destination index, endian and format.
     * (添加long数据，针对特殊EByteBuffFormat的处理)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @param format       EByteBuffFormat format
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putLong(long src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn8Bytes(LongUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a float data by destination index and endian.
     * (添加float数据)
     *
     * @param src          a float data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putFloat(float src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a float data by destination index, endian and format.
     * (添加float数据，针对特殊EByteBuffFormat的处理)
     *
     * @param src          a float data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @param format       EByteBuffFormat format
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putFloat(float src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn4Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a double data by destination index and endian.
     * (添加double数据)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putDouble(double src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn8Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a double data by destination index, endian and format.
     * (添加double数据，针对特殊EByteBuffFormat的处理)
     *
     * @param src          a long data
     * @param desIndex     destination index
     * @param littleEndian is little endian
     * @param format       EByteBuffFormat format
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putDouble(double src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn8Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * Add a string data.
     * (添加字符串)
     *
     * @param src      a string data
     * @param charsets charsets
     * @param desIndex destination index
     * @return ByteWriteBuff itself
     */
    public ByteWriteBuff putString(String src, Charset charsets, int desIndex) {
        return this.putBytes(src.getBytes(charsets), 0, desIndex);
    }
}
