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
     * 将short转换为字节数组
     *
     * @param data short数据
     * @return 字节数组
     */
    public static byte[] toByteArray(long data) {
        return toByteArray(data, false);
    }

    /**
     * 将short转换为字节数组
     *
     * @param data         short数据
     * @param littleEndian true:小端，false：大端
     * @return 字节数组
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
}
