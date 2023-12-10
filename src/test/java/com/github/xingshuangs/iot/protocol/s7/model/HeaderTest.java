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

import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class HeaderTest {

    @Test
    public void byteArrayLength() {
        Header header = new Header();
        assertEquals(10, header.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        Header header = new Header();
        header.setProtocolId((byte) 0x32);
        header.setMessageType(EMessageType.JOB);
        header.setReserved(0x0000);
        header.setPduReference(0x0000);
        header.setParameterLength(0x0000);
        header.setDataLength(0x0002);
//        header.setErrorClass(EErrorClass.NO_ERROR);
//        header.setErrorCode((byte) 0x00);
        byte[] actual = header.toByteArray();
//        byte[] expect = new byte[]{(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        byte[] expect = new byte[]{(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray1() {
        AckHeader header = new AckHeader();
        header.setProtocolId((byte) 0x32);
        header.setMessageType(EMessageType.JOB);
        header.setReserved(0x0000);
        header.setPduReference(0x0000);
        header.setParameterLength(0x0000);
        header.setDataLength(0x0002);
        header.setErrorClass(EErrorClass.NO_ERROR);
        header.setErrorCode((byte) 0x00);
        byte[] actual = header.toByteArray();
        byte[] expect = new byte[]{(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expect, actual);
    }
}