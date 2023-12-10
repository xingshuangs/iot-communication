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

import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlcControlParameterTest {

    @Test
    public void byteArrayLength() {
        PlcControlParameter parameter = new PlcControlParameter();
        assertEquals(11, parameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock(new PlcControlStringParamBlock());
        parameter.setPiService(PlcControlParameter.P_PROGRAM);
        byte[] actual = parameter.toByteArray();
        byte[] expect = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void fromBytes() {
        byte[] data = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};

        PlcControlParameter parameter = PlcControlParameter.fromBytes(data);
        assertEquals(EFunctionCode.PLC_CONTROL, parameter.getFunctionCode());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD}, parameter.getUnknownBytes());
        assertEquals(0, parameter.getParameterBlockLength());
        assertEquals(0, parameter.getParameterBlock().byteArrayLength());
        assertEquals(9, parameter.getLengthPart());
        assertEquals(PlcControlParameter.P_PROGRAM, parameter.getPiService());
    }

    @Test
    public void insert() {
        byte[] expect = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x0A,
                (byte) 0x01, (byte) 0x00,
                (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x50,
                (byte) 0x05, (byte) 0x5F, (byte) 0x49, (byte) 0x4e, (byte) 0x53, (byte) 0x45};

        PlcControlParameter insert = PlcControlParameter.insert(EFileBlockType.DB, 1, EDestinationFileSystem.P);
        assertArrayEquals(expect, insert.toByteArray());

    }
}