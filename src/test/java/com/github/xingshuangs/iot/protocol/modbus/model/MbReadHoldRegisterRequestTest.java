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

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadHoldRegisterRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x6B, (byte) 0x00, (byte) 0x03};
        MbReadHoldRegisterRequest mb = new MbReadHoldRegisterRequest();
        mb.setFunctionCode(EMbFunctionCode.READ_HOLD_REGISTER);
        mb.setAddress(107);
        mb.setQuantity(3);
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x6B, (byte) 0x00, (byte) 0x03};
        MbReadHoldRegisterRequest mb = MbReadHoldRegisterRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_HOLD_REGISTER, mb.functionCode);
        assertEquals(107, mb.getAddress());
        assertEquals(3, mb.getQuantity());
    }
}