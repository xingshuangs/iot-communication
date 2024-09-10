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

package com.github.xingshuangs.iot.protocol.rtp.model.payload;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExpGolombTest {

    @Test
    public void read1Bit() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        int actual = expGolomb.read1Bit();
        assertEquals(0, actual);

        data = new byte[]{(byte) 0x80};
        expGolomb = new ExpGolomb(data);
        actual = expGolomb.read1Bit();
        assertEquals(1, actual);
    }

    @Test
    public void readNBit() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        long actual = expGolomb.readNBit(9);
        assertEquals(30, actual);
    }

    @Test
    public void skipLZ() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        int actual = expGolomb.skipLZ();
        assertEquals(4, actual);
    }

    @Test
    public void skipUE() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        expGolomb.skipUE();
        assertEquals(1, expGolomb.getBitIndex());
    }

    @Test
    public void skipSE() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        expGolomb.skipSE();
        assertEquals(1, expGolomb.getBitIndex());
    }

    @Test
    public void readBoolean() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        boolean actual = expGolomb.readBoolean();
        assertFalse(actual);
    }

    @Test
    public void readUE() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        int actual = expGolomb.readUE();
        assertEquals(29, actual);
    }

    @Test
    public void readSE() {
        byte[] data = new byte[]{0x0F, 0x00};
        ExpGolomb expGolomb = new ExpGolomb(data);
        int actual = expGolomb.readSE();
        assertEquals(15, actual);
    }
}