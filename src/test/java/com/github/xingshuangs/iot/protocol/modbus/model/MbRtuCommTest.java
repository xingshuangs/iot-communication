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

package com.github.xingshuangs.iot.protocol.modbus.model;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class MbRtuCommTest {

    @Test
    public void mbRtuRequestTest() {
        byte[] expect = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 0xC5, (byte) 0xCD};
        MbReadHoldRegisterRequest pdu = new MbReadHoldRegisterRequest(0, 10);
        MbRtuRequest request = new MbRtuRequest(1, pdu);
        byte[] actual = request.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void mbRtuResponseTest() {
        byte[] expect = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x14, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1E, (byte) 0x1C};
        MbRtuResponse response = MbRtuResponse.fromBytes(expect);
        assertEquals(1, response.getUnitId());
        assertArrayEquals(new byte[]{(byte) 0x1E, (byte) 0x1C}, response.getCrc());
        MbReadHoldRegisterResponse holdRegister = (MbReadHoldRegisterResponse) response.getPdu();
        assertEquals(20, holdRegister.getCount());
        byte[] expectData = new byte[]{(byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expectData, holdRegister.getRegister());
    }
}