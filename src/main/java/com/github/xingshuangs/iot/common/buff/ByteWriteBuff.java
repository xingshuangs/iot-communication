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
 * 写字节缓存
 *
 * @author xingshuang
 */
@Getter
public class ByteWriteBuff {

    /**
     * 4字节或8字节的编码格式
     */
    private final EByteBuffFormat format;

    /**
     * 数据
     */
    private final byte[] data;

    /**
     * 偏移量
     */
    private int offset = 0;

    /**
     * 是否为小端模式，默认不是，为大端模式
     */
    private final boolean littleEndian;

    /**
     * 构造方法
     *
     * @param capacity 容量
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
     * 获取指定索引的字节
     *
     * @param index 索引
     * @return 字节数据
     */
    public byte getByte(int index) {
        if (index > data.length - 1) {
            throw new IndexOutOfBoundsException("index");
        }
        return this.data[index];
    }

    /**
     * 校验条件
     *
     * @param desIndex     目标索引
     * @param targetLength 目标长度
     */
    private void checkCondition(int desIndex, int targetLength) {
        if (desIndex + targetLength > data.length) {
            // 超过字节数组最大容量
            throw new IllegalArgumentException("Exceeds the maximum capacity of the byte array");
        }
    }

    /**
     * 添加字节数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putByte(byte src) {
        return this.putByte(src, this.offset);
    }

    public ByteWriteBuff putByte(byte src, int desIndex) {
        this.checkCondition(desIndex, 1);
        this.data[desIndex] = src;
        if (this.offset == desIndex) {
            this.offset++;
        }
        return this;
    }

    /**
     * 添加int类型单字节数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putByte(int src) {
        return this.putByte(ByteUtil.toByte(src));
    }

    /**
     * 添加字节数组数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putBytes(byte[] src) {
        return this.putBytes(src, 0, this.offset);
    }

    /**
     * 添加字节数组数据
     *
     * @param src      数据源
     * @param srcIndex 起始索引
     * @return 对象本身
     */
    public ByteWriteBuff putBytes(byte[] src, int srcIndex) {
        return this.putBytes(src, srcIndex, this.offset);
    }

    /**
     * 添加字节数组数据，当desIndex==this.offset时，才将this.offset进行偏移，否则保持不变
     *
     * @param src      数据源
     * @param srcIndex 起始索引
     * @param desIndex 目标索引
     * @return 对象本身
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
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putShort(short src) {
        return this.putShort(src, this.offset, this.littleEndian);
    }

    /**
     * 添加short数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putShort(short src, int desIndex) {
        return this.putShort(src, desIndex, this.littleEndian);
    }

    /**
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putShort(int src) {
        return this.putBytes(ShortUtil.toByteArray(src, this.littleEndian));
    }

    /**
     * 添加short数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putShort(int src, int desIndex) {
        return this.putBytes(ShortUtil.toByteArray(src, this.littleEndian), 0, desIndex);
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(int src) {
        return this.putInteger(src, this.offset, this.littleEndian);
    }

    /**
     * 添加Integer数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(int src, int desIndex) {
        return this.putInteger(src, desIndex, this.littleEndian);
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src) {
        return this.putInteger(src, this.offset, this.littleEndian);
    }

    /**
     * 添加Integer数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src, int desIndex) {
        return this.putInteger(src, desIndex, this.littleEndian);
    }

    /**
     * 添加long数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src) {
        return this.putLong(src, this.offset, this.littleEndian);
    }

    /**
     * 添加long数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src, int desIndex) {
        return this.putLong(src, desIndex, this.littleEndian);
    }

    /**
     * 添加float数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src) {
        return this.putFloat(src, this.offset, this.littleEndian);
    }

    /**
     * 添加float数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src, int desIndex) {
        return this.putFloat(src, desIndex, this.littleEndian);
    }

    /**
     * 添加double数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src) {
        return this.putDouble(src, this.offset, this.littleEndian);
    }

    /**
     * 添加double数据
     *
     * @param src      数据源
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src, int desIndex) {
        return this.putDouble(src, desIndex, this.littleEndian);
    }

    /**
     * 添加string数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putString(String src) {
        return this.putString(src, StandardCharsets.US_ASCII, this.offset);
    }

    /**
     * 添加string数据
     *
     * @param src      数据源
     * @param charsets 字符集类型
     * @return 对象本身
     */
    public ByteWriteBuff putString(String src, Charset charsets) {
        return this.putString(src, charsets, this.offset);
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putShort(short src, int desIndex, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian), 0, desIndex);
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putShort(int src, int desIndex, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian), 0, desIndex);
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(int src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(IntegerUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(IntegerUtil.toByteArray((int) src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加integer数据，针对特殊EByteBuffFormat的处理
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @param format       与自身ByteWriteBuff不一致的EByteBuffFormat
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn4Bytes(IntegerUtil.toByteArray((int) src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加long数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn8Bytes(LongUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加long数据，针对特殊EByteBuffFormat的处理
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @param format       与自身ByteWriteBuff不一致的EByteBuffFormat
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn8Bytes(LongUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加float数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn4Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加float数据，针对特殊EByteBuffFormat的处理
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @param format       与自身ByteWriteBuff不一致的EByteBuffFormat
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn4Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加double数据
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src, int desIndex, boolean littleEndian) {
        return this.putBytes(this.format.formatIn8Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加double数据，针对特殊EByteBuffFormat的处理
     *
     * @param src          数据源
     * @param desIndex     目标索引
     * @param littleEndian 是否小端模式
     * @param format       与自身ByteWriteBuff不一致的EByteBuffFormat
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src, int desIndex, boolean littleEndian, EByteBuffFormat format) {
        return this.putBytes(format.formatIn8Bytes(FloatUtil.toByteArray(src, littleEndian)), 0, desIndex);
    }

    /**
     * 添加字符串
     *
     * @param src      数据源
     * @param charsets 字符集
     * @param desIndex 目标索引
     * @return 对象本身
     */
    public ByteWriteBuff putString(String src, Charset charsets, int desIndex) {
        return this.putBytes(src.getBytes(charsets), 0, desIndex);
    }
}
