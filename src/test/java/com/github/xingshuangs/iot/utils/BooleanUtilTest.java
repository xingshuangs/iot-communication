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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class BooleanUtilTest {

    @Test
    public void setBit() {
        byte actual = BooleanUtil.setBit((byte) 0x70, 7, true);
        assertEquals((byte) 0xF0, actual);
        actual = BooleanUtil.setBit((byte) 0x70, 7, true);
        assertEquals((byte) 0xF0, actual);
        actual = BooleanUtil.setBit((byte) 0x30, 6, false);
        assertEquals((byte) 0x30, actual);
        actual = BooleanUtil.setBit((byte) 0x50, 5, false);
        assertEquals((byte) 0x50, actual);
        actual = BooleanUtil.setBit((byte) 0x60, 4, false);
        assertEquals((byte) 0x60, actual);
        actual = BooleanUtil.setBit((byte) 0x78, 3, true);
        assertEquals((byte) 0x78, actual);
        actual = BooleanUtil.setBit((byte) 0x74, 2, true);
        assertEquals((byte) 0x74, actual);
        actual = BooleanUtil.setBit((byte) 0x73, 1, true);
        assertEquals((byte) 0x73, actual);
        actual = BooleanUtil.setBit((byte) 0x71, 0, true);
        assertEquals((byte) 0x71, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setBitException() {
        BooleanUtil.setBit((byte) 0x70, 8, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setBitException1() {
        BooleanUtil.setBit((byte) 0x70, -1, true);
    }

    @Test
    public void getValue() {
        boolean b = BooleanUtil.getValue((byte) 0x80, 7);
        assertTrue(b);
        b = BooleanUtil.getValue((byte) 0x80, 6);
        assertFalse(b);
    }

    @Test
    public void byteArrayToListTest() {
        byte[] data = new byte[]{(byte) 0xF5, 0x51};
        List<Boolean> booleanList = BooleanUtil.byteArrayToList(11, data);
        assertTrue(booleanList.get(0));
        assertFalse(booleanList.get(1));
        assertTrue(booleanList.get(2));
        assertFalse(booleanList.get(3));
        assertTrue(booleanList.get(4));
        assertTrue(booleanList.get(5));
        assertTrue(booleanList.get(6));
        assertTrue(booleanList.get(7));
        assertTrue(booleanList.get(8));
        assertFalse(booleanList.get(9));
        assertFalse(booleanList.get(10));
    }

    @Test
    public void listToByteArrayTest() {
        byte[] expect = new byte[]{(byte) 0x55, 0x05};
        List<Boolean> booleans = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            booleans.add(i % 2 == 0);
        }
        byte[] bytes = BooleanUtil.listToByteArray(booleans);
        assertArrayEquals(expect, bytes);
    }
}