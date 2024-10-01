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
import java.util.Arrays;

/**
 * Read byte array buffer class.
 * (字节读取缓存)
 *
 * @author xingshuang
 */
@Getter
public class ByteReadBuff {

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
    private int offset;

    /**
     * Is little endian. The default is not, big endian mode.
     * (是否为小端模式，默认不是，为大端模式)
     */
    private final boolean littleEndian;

    /**
     * Gets the number of remaining bytes
     * (获取剩余字节数量)
     *
     * @return the number of remaining bytes
     */
    public int getRemainSize() {
        return this.data.length - this.offset;
    }

    /**
     * Construct.
     *
     * @param data byte array
     */
    public ByteReadBuff(byte[] data) {
        this(data, 0, false, EByteBuffFormat.DC_BA);
    }

    public ByteReadBuff(byte[] data, int offset) {
        this(data, offset, false, EByteBuffFormat.DC_BA);
    }

    public ByteReadBuff(byte[] data, int offset, boolean littleEndian) {
        this(data, offset, littleEndian, EByteBuffFormat.DC_BA);
    }

    public ByteReadBuff(byte[] data, EByteBuffFormat format) {
        this(data, 0, false, format);
    }

    public ByteReadBuff(byte[] data, boolean littleEndian) {
        this(data, 0, littleEndian, EByteBuffFormat.DC_BA);
    }

    public ByteReadBuff(byte[] data, int offset, boolean littleEndian, EByteBuffFormat format) {
        this.littleEndian = littleEndian;
        this.data = data;
        this.offset = offset;
        this.format = format;
    }

    public static ByteReadBuff newInstance(byte[] data) {
        return new ByteReadBuff(data);
    }

    public static ByteReadBuff newInstance(byte[] data, int offset) {
        return new ByteReadBuff(data, offset);
    }

    public static ByteReadBuff newInstance(byte[] data, int offset, boolean littleEndian) {
        return new ByteReadBuff(data, offset, littleEndian);
    }

    public static ByteReadBuff newInstance(byte[] data, boolean littleEndian) {
        return new ByteReadBuff(data, littleEndian);
    }

    public static ByteReadBuff newInstance(byte[] data, EByteBuffFormat format) {
        return new ByteReadBuff(data, format);
    }

    public static ByteReadBuff newInstance(byte[] data, int offset, boolean littleEndian, EByteBuffFormat format) {
        return new ByteReadBuff(data, offset, littleEndian, format);
    }

    /**
     * Check condition.
     * (校验条件）
     *
     * @param index index number
     */
    private void checkCondition(int index) {
        if (index < 0) {
            // 索引不能小于0
            throw new IndexOutOfBoundsException("Index less than 0");
        }
        if (index >= data.length) {
            // 超过字节数组最大容量
            throw new IndexOutOfBoundsException("Exceeds the maximum capacity of the byte array");
        }
    }

    /**
     * Gets boolean type data.
     * (获取boolean类型数据)
     *
     * @param bit bit index
     * @return boolean type data
     */
    public boolean getBoolean(int bit) {
        boolean res = this.getBoolean(this.offset, bit);
        this.offset++;
        return res;
    }

    /**
     * Get one byte data.
     * (获取1个字节数据)
     *
     * @return byte data
     */
    public byte getByte() {
        byte res = this.getByte(this.offset);
        this.offset++;
        return res;
    }

    /**
     * Gets all remaining bytes.
     * (获取剩余所有字节)
     *
     * @return byte array
     */
    public byte[] getBytes() {
        int length = this.data.length - this.offset;
        return this.getBytes(length);
    }

    /**
     * Get byte array data by length.
     * (获取字节数组数据)
     *
     * @param length length
     * @return byte array
     */
    public byte[] getBytes(int length) {
        byte[] res = this.getBytes(this.offset, length);
        this.offset += length;
        return res;
    }

    /**
     * Get int data from one byte.
     * (获取1个字节的整形数据
     *
     * @return int data
     */
    public int getByteToInt() {
        int res = this.getByteToInt(this.offset);
        this.offset++;
        return res;
    }

    /**
     * Get one int16 data.
     * (获取int16数据)
     *
     * @return int16 data
     */
    public short getInt16() {
        short res = this.getInt16(this.offset);
        this.offset += 2;
        return res;
    }

    /**
     * Get one uint16 data.
     * (获取uint16数据)
     *
     * @return uint16 data
     */
    public int getUInt16() {
        int res = this.getUInt16(this.offset);
        this.offset += 2;
        return res;
    }

    /**
     * Get one int32 data.
     * (获取int32数据)
     *
     * @return int32 data
     */
    public int getInt32() {
        int res = this.getInt32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * Get one uint32 data.
     * (获取uint32数据)
     *
     * @return uint32 data
     */
    public long getUInt32() {
        long res = this.getUInt32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * Get one int64 data.
     * (获取int32数据)
     *
     * @return int64 data
     */
    public long getInt64() {
        long res = this.getInt64(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * Get one float32 data.
     * (获取float32数据)
     *
     * @return float32 data
     */
    public float getFloat32() {
        float res = this.getFloat32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * Get one uint64 data.
     * (获取float64数据)
     *
     * @return float64 data
     */
    public double getFloat64() {
        double res = this.getFloat64(this.offset);
        this.offset += 8;
        return res;
    }

    /**
     * Get string by length
     * (获取字符串数据)
     *
     * @param length string length
     * @return string data
     */
    public String getString(int length) {
        String res = this.getString(this.offset, length);
        this.offset += length;
        return res;
    }

    /**
     * Get string with charset by length
     * (获取字符串数据)
     *
     * @param length  string length
     * @param charset target charset
     * @return string data
     */
    public String getString(int length, Charset charset) {
        String res = this.getString(this.offset, length, charset);
        this.offset += length;
        return res;
    }

    /**
     * Get one boolean data by byte index and bit index.
     * (获取boolean数据)
     *
     * @param index byte index
     * @param bit   bit index
     * @return boolean data
     */
    public boolean getBoolean(int index, int bit) {
        this.checkCondition(index);
        return BooleanUtil.getValue(this.data[index], bit);
    }

    /**
     * Get one int data from boolean data by byte index and bit index.
     * (获取一位的int值数据)
     *
     * @param index byte index
     * @param bit   bit index
     * @return boolean data
     */
    public int getBitToInt(int index, int bit) {
        this.checkCondition(index);
        return BooleanUtil.getValueToInt(this.data[index], bit);
    }

    /**
     * Get one byte by byte index
     * (获取字节数据)
     *
     * @param index byte index
     * @return byte data
     */
    public byte getByte(int index) {
        this.checkCondition(index);
        return this.data[index];
    }

    /**
     * Get byte array by byte index and length
     * (获取字节数组数据)
     *
     * @param index  byte index
     * @param length byte length
     * @return byte array
     */
    public byte[] getBytes(int index, int length) {
        this.checkCondition(index + length - 1);
        return Arrays.copyOfRange(this.data, index, index + length);
    }

    /**
     * Get one int data from one byte by byte index
     * (获取一个字节的整形数据)
     *
     * @param index byte index
     * @return int data
     */
    public int getByteToInt(int index) {
        this.checkCondition(index);
        return ByteUtil.toUInt8(this.data, index);
    }

    /**
     * Gets the combination of several bits in a byte
     * (获取一个字节中几个位组合而成的数据)
     *
     * @param index         byte index
     * @param startBitIndex start bit index
     * @param bitLength     bit length
     * @return int data
     */
    public int getByteToInt(int index, int startBitIndex, int bitLength) {
        this.checkCondition(index);
        return ByteUtil.toUInt8(this.data[index], startBitIndex, bitLength);
    }

    /**
     * Get int16 data by byte index.
     * (获取int16数据)
     *
     * @param index byte index
     * @return int16 data
     */
    public short getInt16(int index) {
        this.checkCondition(index);
        return ShortUtil.toInt16(this.data, index, this.littleEndian);
    }

    /**
     * Get uint16 data by byte index.
     * (获取uint16数据)
     *
     * @param index byte index
     * @return uint16 data
     */
    public int getUInt16(int index) {
        this.checkCondition(index);
        return ShortUtil.toUInt16(this.data, index, this.littleEndian);
    }

    /**
     * Get int32 data by byte index.
     * (获取int32数据)
     *
     * @param index byte index
     * @return int32 data
     */
    public int getInt32(int index) {
        this.checkCondition(index);
        return IntegerUtil.toInt32(this.format.formatIn4Bytes(this.data, index), 0, this.littleEndian);
    }

    /**
     * Get uint32 data by byte index.
     * (获取uint32数据)
     *
     * @param index byte index
     * @return uint32 data
     */
    public long getUInt32(int index) {
        this.checkCondition(index);
        return IntegerUtil.toUInt32(this.format.formatIn4Bytes(this.data, index), 0, this.littleEndian);
    }

    /**
     * Get int64 data by byte index.
     * (获取int64数据)
     *
     * @param index byte index
     * @return int64 data
     */
    public long getInt64(int index) {
        this.checkCondition(index);
        return LongUtil.toInt64(this.format.formatIn8Bytes(this.data, index), 0, this.littleEndian);
    }

    /**
     * Get float32 data by byte index.
     * (获取float32数据)
     *
     * @param index byte index
     * @return float32 data
     */
    public float getFloat32(int index) {
        this.checkCondition(index);
        return FloatUtil.toFloat32(this.format.formatIn4Bytes(this.data, index), 0, this.littleEndian);
    }

    /**
     * Get float64 data by byte index.
     * (获取float64数据)
     *
     * @param index byte index
     * @return float64 data
     */
    public double getFloat64(int index) {
        this.checkCondition(index);
        return FloatUtil.toFloat64(this.format.formatIn8Bytes(this.data, index), 0, this.littleEndian);
    }

    /**
     * Get string by condition.
     * (获取字符串)
     *
     * @param index  byte index
     * @param length byte length
     * @return string data
     */
    public String getString(int index, int length) {
        return this.getString(index, length, StandardCharsets.US_ASCII);
    }

    /**
     * Get string by condition.
     * (获取字符串)
     *
     * @param index   byte index
     * @param length  byte length
     * @param charset target charset
     * @return string data
     */
    public String getString(int index, int length, Charset charset) {
        this.checkCondition(index + length - 1);
        return ByteUtil.toStr(this.data, index, length, charset);
    }
}
