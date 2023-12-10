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
import org.junit.Test;

import static org.junit.Assert.*;

public class HexUtilTest {

    @Test
    public void toHexArray_One() {
        byte[] bytes = HexUtil.toHexArray("1A");
        assertEquals(0x1A, bytes[0]);
    }

    @Test
    public void toHexArray_Multi() {
        byte[] actual = HexUtil.toHexArray("1a6BdE8c");
        byte[] expected = new byte[]{(byte) 0x1A, (byte) 0x6B, (byte) 0xDE, (byte) 0x8C};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_NotEven() {
        HexUtil.toHexArray("1a6BdE8");
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_Null() {
        HexUtil.toHexArray(null);
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_Empty() {
        HexUtil.toHexArray("");
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_NotHex() {
        HexUtil.toHexArray("ii");
    }

    @Test
    public void toHexString() {
        String expected = "1A 6B DE 8C";
        byte[] data = new byte[]{(byte) 0x1A, (byte) 0x6B, (byte) 0xDE, (byte) 0x8C};
        String actual = HexUtil.toHexString(data);
        assertEquals(expected, actual);

        expected = "A1 49 AB DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data);
        assertEquals(expected, actual);

        expected = "A1|49|AB|DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"|");
        assertEquals(expected, actual);

        expected = "A1-49-AB-DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"-");
        assertEquals(expected, actual);

        expected = "A1@49@AB@DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"@");
        assertEquals(expected, actual);
    }
}
