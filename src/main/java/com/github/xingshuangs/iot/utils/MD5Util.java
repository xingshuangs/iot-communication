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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 tool.
 * (MD5工具)
 *
 * @author xingshuang
 */
public class MD5Util {

    private MD5Util() {
        // NOOP
    }

    /**
     * Encode string
     *
     * @param src string
     * @return result
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String encode(String src) throws NoSuchAlgorithmException {
        return encode(src, StandardCharsets.US_ASCII);
    }

    /**
     * Encode string.
     *
     * @param src      source string
     * @param charsets charset
     * @return result
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String encode(String src, Charset charsets) throws NoSuchAlgorithmException {
        byte[] bytes = src.getBytes(charsets);
        return encode(bytes);
    }

    /**
     * Encode string.
     *
     * @param src byte array
     * @return result
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String encode(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(src);
        return HexUtil.toHexString(bytes, "", false);
    }
}
