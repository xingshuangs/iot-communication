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

import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import org.junit.Test;

import static org.junit.Assert.*;


public class Mc7FileTest {

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{
                (byte) 0x70, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x08, (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x91, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0xd1, (byte) 0xb8, (byte) 0x41, (byte) 0x38, (byte) 0x9c, (byte) 0x02, (byte) 0xd1,
                (byte) 0xb8, (byte) 0x0e, (byte) 0x38, (byte) 0x9c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10,
        };
        Mc7File mc7File = Mc7File.fromBytes(data);
        assertEquals(36, mc7File.getData().length);
        assertEquals(EFileBlockType.OB, mc7File.getBlockType());
        assertEquals(1, mc7File.getBlockNumber());
        assertEquals(16, mc7File.getMC7CodeLength());
    }
}