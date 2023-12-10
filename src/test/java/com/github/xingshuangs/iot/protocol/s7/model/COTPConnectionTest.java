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

package com.github.xingshuangs.iot.protocol.s7.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class COTPConnectionTest {

    @Test
    public void byteArrayLength() {
        COTPConnection connection = new COTPConnection();
        assertEquals(18, connection.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        COTPConnection connection = COTPConnection.crConnectRequest(0x0100,0x0100);
        byte[] actual = connection.toByteArray();
        byte[] expect = {(byte) 0x11, (byte) 0xE0,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00,
                (byte) 0xC0,
                (byte) 0x01,
                (byte) 0x0A,
                (byte) 0xC1,
                (byte) 0x02,
                (byte) 0x01, (byte) 0x00,
                (byte) 0xC2,
                (byte) 0x02,
                (byte) 0x01, (byte) 0x00};

        assertArrayEquals(expect, actual);
    }
}