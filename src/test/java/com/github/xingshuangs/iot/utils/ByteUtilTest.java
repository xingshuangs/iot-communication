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


public class ByteUtilTest {

    @Test
    public void toByte() {
        byte actual = ByteUtil.toByte(11);
        assertEquals((byte) 0x0B, actual);
    }

    @Test
    public void toUInt8() {
        int actual = ByteUtil.toUInt8((byte) 0xFF);
        assertEquals(255, actual);
    }

    @Test
    public void toStr() {
        byte[] data = {(byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};
        String actual = ByteUtil.toStr(data);
        assertEquals("P_PROGRAM", actual);
    }
}