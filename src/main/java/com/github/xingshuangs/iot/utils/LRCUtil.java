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
 * LRC tool.
 * LRC校验工具
 *
 * @author xingshuang
 */
public class LRCUtil {

    private LRCUtil() {
        // NOOP
    }

    /**
     * Get LRC code.
     * (获取lrc码)
     *
     * @param src byte array
     * @return byte
     */
    public static byte lrc(byte[] src) {
        if (src == null || src.length == 0) {
            throw new IllegalArgumentException("src");
        }

        int sum = 0;
        for (byte b : src) {
            sum += b;
        }
        sum = sum % 256;
        sum = 256 - sum;
        return (byte) sum;
    }

    /**
     * Check LRC.
     * (lrc校验)
     *
     * @param src    byte array
     * @param target target result
     * @return true：equality，false：inequality
     */
    public static boolean lrc(byte[] src, byte target) {
        byte des = lrc(src);
        return des == target;
    }
}
