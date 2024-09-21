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
public class LongUtil {

    private LongUtil() {

    }

    /**
     * Convert long to a 8-byte array.
     * (将long转换为字节数组)
     *
     * @param data long data
     * @return byte array
     */
    public static byte[] toByteArray(long data) {
        return toByteArray(data, false);
    }

    /**
     * Convert long to a 8-byte array.
     * (将long转换为字节数组)
     *
     * @param data         long data
     * @param littleEndian true: little endian，false：big endian
     * @return byte array
     */
    public static byte[] toByteArray(long data, boolean littleEndian) {
        byte[] bytes = new byte[8];

        if (littleEndian) {
            bytes[0] = (byte) ((data) & 0xFF);
            bytes[1] = (byte) ((data >> 8) & 0xFF);
            bytes[2] = (byte) ((data >> 16) & 0xFF);
            bytes[3] = (byte) ((data >> 24) & 0xFF);
            bytes[4] = (byte) ((data >> 32) & 0xFF);
            bytes[5] = (byte) ((data >> 40) & 0xFF);
            bytes[6] = (byte) ((data >> 48) & 0xFF);
            bytes[7] = (byte) ((data >> 56) & 0xFF);
        } else {
            bytes[0] = (byte) ((data >> 56) & 0xFF);
            bytes[1] = (byte) ((data >> 48) & 0xFF);
            bytes[2] = (byte) ((data >> 40) & 0xFF);
            bytes[3] = (byte) ((data >> 32) & 0xFF);
            bytes[4] = (byte) ((data >> 24) & 0xFF);
            bytes[5] = (byte) ((data >> 16) & 0xFF);
            bytes[6] = (byte) ((data >> 8) & 0xFF);
            bytes[7] = (byte) ((data) & 0xFF);
        }
        return bytes;
    }

    /**
     * Convert byte array to int64.
     * (将字节数组转换为int64)
     *
     * @param data byte array
     * @return int64 data
     */
    public static long toInt64(byte[] data) {
        return toInt64(data, 0, false);
    }

    /**
     * Convert byte array to int64.
     * (将字节数组转换为int64)
     *
     * @param data   byte array
     * @param offset offset
     * @return int64 data
     */
    public static long toInt64(byte[] data, int offset) {
        return toInt64(data, offset, false);
    }

    /**
     * Convert byte array to int64.
     * (将字节数组转换为int64)
     *
     * @param data         byte array
     * @param offset       offset
     * @param littleEndian true：little endian，false：big endian
     * @return int64 data
     */
    public static long toInt64(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("data length < 8");
        }
        if (offset + 8 > data.length) {
            throw new IndexOutOfBoundsException("offset + 8 > data length");
        }
        int b = littleEndian ? 7 : 0;
        int d = littleEndian ? 1 : -1;
        return (((long) (data[offset + b - d * 0] & 0xFF) << 56)
                | ((long) (data[offset + b - d * 1] & 0xFF) << 48)
                | ((long) (data[offset + b - d * 2] & 0xFF) << 40)
                | ((long) (data[offset + b - d * 3] & 0xFF) << 32)
                | ((long) (data[offset + b - d * 4] & 0xFF) << 24)
                | ((long) (data[offset + b - d * 5] & 0xFF) << 16)
                | ((long) (data[offset + b - d * 6] & 0xFF) << 8)
                | ((long) (data[offset + b - d * 7] & 0xFF) << 0));
    }
}
