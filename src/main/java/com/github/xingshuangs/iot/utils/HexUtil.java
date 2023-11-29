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


import com.github.xingshuangs.iot.exceptions.HexParseException;

/**
 * @author xingshuang
 */
public class HexUtil {

    private HexUtil() {
        // do nothing
    }

    /**
     * 验证16进制字符串的正则表达式
     * ^ = 开始
     * $ = 结束
     * + = 匹配前面的子表达式一次或多次。
     * [] = 表达式的开始和结束
     */
    private static final String REGEX = "^[a-f0-9A-F]+$";

    /**
     * 将字符串转换为16进制的数组
     *
     * @param src 字符串
     * @return 字节数组
     */
    public static byte[] toHexArray(String src) {
        if (src == null || src.length() == 0) {
            throw new HexParseException("字符串不能为null或长度不能为0");
        }
        if ((src.length() & -src.length()) == 1) {
            throw new HexParseException("输入的字符串个数必须为偶数");
        }
        if (!src.matches(REGEX)) {
            throw new HexParseException("字符串内容必须是[0-9|a-f|A-F]");
        }

        char[] chars = src.toCharArray();
        final byte[] out = new byte[chars.length >> 1];
        for (int i = 0; i < chars.length; i = i + 2) {
            int high = Character.digit(chars[i], 16) << 4;
            int low = Character.digit(chars[i + 1], 16);
            out[i / 2] = (byte) ((high | low) & 0xFF);
        }
        return out;
    }

    /**
     * 将字节数组转换为16进制字符串，并且默认按空格隔开
     *
     * @param src 字节数组
     * @return 字符串
     */
    public static String toHexString(byte[] src) {
        return toHexString(src, " ", true);
    }

    /**
     * 将字节数组转换为16进制字符串，并且默认按指定字符串隔开
     *
     * @param src      字节数组
     * @param splitStr 分隔字符串
     * @return 字符串
     */
    public static String toHexString(byte[] src, String splitStr) {
        return toHexString(src, splitStr, true);
    }

    /**
     * 将字节数组转换为16进制字符串，并且默认按指定字符串隔开
     *
     * @param src       字节数组
     * @param splitStr  分隔字符串
     * @param upperCase 大写
     * @return 字符串
     */
    public static String toHexString(byte[] src, String splitStr, boolean upperCase) {
        if (src == null || src.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String format = upperCase ? "%02X" : "%02x";
        for (int i = 0; i < src.length; i++) {
            sb.append(String.format(format, src[i]));
            if (i != src.length - 1) {
                sb.append(splitStr);
            }
        }
        return sb.toString().trim();
    }
}
