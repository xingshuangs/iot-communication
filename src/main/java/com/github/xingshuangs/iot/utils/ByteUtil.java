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

package com.github.xingshuangs.iot.utils;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author xingshuang
 */
public class ByteUtil {

    private ByteUtil() {
        // NOOP
    }

    /**
     * 将int转换为byte
     *
     * @param data int数据
     * @return byte数据
     */
    public static byte toByte(int data) {
        return (byte) (data & 0xFF);
    }

    /**
     * 将byte转换为 uint8
     *
     * @param data byte数据
     * @return uint8数据
     */
    public static int toUInt8(byte data) {
        return data & 0xFF;
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data 字节数组
     * @return int32数据
     */
    public static int toUInt8(byte[] data) {
        return toUInt8(data, 0);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data   字节数组
     * @param offset index offset
     * @return int32数据
     */
    public static int toUInt8(byte[] data, int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("data length < 1");
        }
        if (offset + 1 > data.length) {
            throw new IndexOutOfBoundsException("offset + 1 > data length");
        }
        return (data[offset] & 0xFF);
    }

    /**
     * 获取字节指定范围bit的数值
     *
     * @param data          字节数据
     * @param startBitIndex 位索引
     * @param bitLength     位长度
     * @return int值
     */
    public static int toUInt8(byte data, int startBitIndex, int bitLength) {
        if (startBitIndex < 0 || startBitIndex > 7) {
            throw new IllegalArgumentException("The start bit index can only be[0,7]");
        }
        if (startBitIndex + bitLength > 8) {
            throw new IllegalArgumentException("startBitIndex + length > 8");
        }
        return (data >> startBitIndex) & (((int) Math.pow(2, bitLength) - 1) & 0xFF);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data 字节数组
     * @return 字符串
     */
    public static String toStr(byte[] data) {
        return toStr(data, 0, data.length, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data   字节数组
     * @param offset index offset
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset) {
        return toStr(data, offset, data.length - offset, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data   字节数组
     * @param offset index offset
     * @param length 长度
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset, int length) {
        return toStr(data, offset, length, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data        字节数组
     * @param offset      偏移量
     * @param length      长度
     * @param charsetName 字符编码名称
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset, int length, Charset charsetName) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        if (offset > data.length || offset + length > data.length) {
            throw new IllegalArgumentException("offset > data length || offset + length > data length");
        }
        return new String(data, offset, length, charsetName).trim();
    }
}
