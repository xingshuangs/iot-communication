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
 * BCD code conversion tool.
 * (BCD码转换工具)
 *
 * @author xingshuang
 */
public class BCDUtil {

    private BCDUtil() {
        // NOOP
    }

    /**
     * Byte to int by BCD.
     * 转换为int
     *
     * @param data byte data
     * @return int result
     */
    public static int toInt(byte data) {
        return ((data >> 4) * 10) + (data & 0x0F);
    }

    /**
     * Byte array to int by BCD.
     *
     * @param data byte array
     * @return int result
     */
    public static int toInt(byte[] data) {
        int result = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            result += toInt(data[i]) * Math.pow(100, i);
        }
        return result;
    }

    /**
     * Int to byte by BCD.
     *
     * @param data int data
     * @return byte
     */
    public static byte toByte(int data) {
        if (data > 99 || data < 0) {
            throw new IllegalArgumentException("data > 99 || data < 0");
        }
        return (byte) (((data / 10) << 4) | (data % 10));
    }
}
