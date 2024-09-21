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


/**
 * @author xingshuang
 */
public class ShortUtil {

    private ShortUtil() {
        // NOOP
    }

    /**
     * Convert int to a 2-byte array.
     * (将int转换为字节数组)
     *
     * @param data         int data
     * @param littleEndian true: little endian，false：big endian
     * @return byte array
     */
    public static byte[] toByteArray(int data, boolean littleEndian) {
        byte[] bytes = new byte[2];

        if (littleEndian) {
            bytes[0] = (byte) (data & 0xFF);
            bytes[1] = (byte) (data >> 8 & 0xFF);
        } else {
            bytes[0] = (byte) (data >> 8 & 0xFF);
            bytes[1] = (byte) (data & 0xFF);
        }
        return bytes;
    }

    /**
     * Convert int to a 2-byte array, using big-endian mode by default.
     * (将int转换为字节数组)
     *
     * @param data int data
     * @return byte array
     */
    public static byte[] toByteArray(int data) {
        return toByteArray(data, false);
    }

    /**
     * Convert short to a 2-byte array, using big-endian mode by default.
     * (将short转换为字节数组)
     *
     * @param data short data
     * @return byte array
     */
    public static byte[] toByteArray(short data) {
        return toByteArray(data, false);
    }

    /**
     * Converts the byte array to int16.
     * (将字节数组转换为int16)
     *
     * @param data byte array
     * @return int16 data
     */
    public static short toInt16(byte[] data) {
        return toInt16(data, 0, false);
    }

    /**
     * Converts the byte array to int16.
     * (将字节数组转换为int16)
     *
     * @param data   byte array
     * @param offset offset
     * @return int16 data
     */
    public static short toInt16(byte[] data, int offset) {
        return toInt16(data, offset, false);
    }

    /**
     * Converts the byte array to int16.
     * (将字节数组转换为int16)
     *
     * @param data         byte array
     * @param offset       offset
     * @param littleEndian true：little endian，false：big endian
     * @return int16 data
     */
    public static short toInt16(byte[] data, int offset, boolean littleEndian) {
        return (short) toUInt16(data, offset, littleEndian);
    }

    /**
     * Converts the byte array to uint16.
     * (将字节数组转换为uint16)
     *
     * @param data byte array
     * @return int16 data
     */
    public static int toUInt16(byte[] data) {
        return toUInt16(data, 0, false);
    }

    /**
     * Converts the byte array to uint16.
     * (将字节数组转换为uint16)
     *
     * @param data   byte array
     * @param offset offset
     * @return int16 data
     */
    public static int toUInt16(byte[] data, int offset) {
        return toUInt16(data, offset, false);
    }

    /**
     * Converts the byte array to uint16.
     * (将字节数组转换为uint16)
     *
     * @param data         byte array
     * @param offset       offset
     * @param littleEndian true：little endian，false：big endian
     * @return int16 data
     */
    public static int toUInt16(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 2) {
            throw new IndexOutOfBoundsException("data length < 2");
        }
        if (offset + 2 > data.length) {
            throw new IndexOutOfBoundsException("offset + 2 > data length");
        }
        int b = littleEndian ? 1 : 0;
        int d = littleEndian ? 1 : -1;
        return (((data[offset + b - d * 0] & 0xFF) << 8)
                | (data[offset + b - d * 1] & 0xFF) << 0);
    }
}
