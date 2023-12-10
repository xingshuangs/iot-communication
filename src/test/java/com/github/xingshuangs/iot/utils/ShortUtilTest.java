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

import org.junit.Test;

import static org.junit.Assert.*;


public class ShortUtilTest {

    @Test
    public void toByteArray() {
        byte[] actual = ShortUtil.toByteArray(24565);
        byte[] expect = new byte[]{(byte) 0x5F, (byte) 0xF5};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray1() {
        byte[] actual = ShortUtil.toByteArray(24565, true);
        byte[] expect = new byte[]{(byte) 0xF5, (byte) 0x5F};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toInt16() {
        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xFF};
        short actual = ShortUtil.toInt16(data);
        assertEquals(-1, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toInt16(data);
        assertEquals(32767, actual);

        data = new byte[]{(byte) 0xFF, (byte) 0x7F};
        actual = ShortUtil.toInt16(data, 0, true);
        assertEquals(32767, actual);
    }

    @Test
    public void toUInt16() {
        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xFF};
        int actual = ShortUtil.toUInt16(data);
        assertEquals(65535, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toUInt16(data);
        assertEquals(32767, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toUInt16(data, 0, true);
        assertEquals(65407, actual);
    }
}