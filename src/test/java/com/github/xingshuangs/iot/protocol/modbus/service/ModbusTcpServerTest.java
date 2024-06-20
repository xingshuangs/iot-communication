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

package com.github.xingshuangs.iot.protocol.modbus.service;

import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class ModbusTcpServerTest {

    private ModbusTcpServer server;
    private ModbusTcp modbusTcp;

    @Before
    public void init() {
        this.server = new ModbusTcpServer();
        this.server.start(503);
        this.modbusTcp = new ModbusTcp("127.0.0.1", 503);
        this.modbusTcp.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
    }

    @After
    public void destroy() {
        this.modbusTcp.close();
        this.server.stop();
    }

    @Test
    public void readCoil() {
        this.modbusTcp.writeCoil(0, true);
        List<Boolean> booleans = this.modbusTcp.readCoil(0, 1);
        assertEquals(1, booleans.size());
        assertTrue(booleans.get(0));
        this.modbusTcp.writeCoil(0, false);
        booleans = this.modbusTcp.readCoil(0, 1);
        assertEquals(1, booleans.size());
        assertFalse(booleans.get(0));

        Boolean[] expect = new Boolean[]{true, false, true, false};
        List<Boolean> list = Arrays.asList(true, false, true, false);
        this.modbusTcp.writeCoil(0, list);
        booleans = this.modbusTcp.readCoil(0, 4);
        assertArrayEquals(expect, booleans.toArray(new Boolean[0]));
    }

    @Test(expected = ModbusCommException.class)
    public void readCoil1() {
        this.modbusTcp.readCoil(1, 2001);
    }

    @Test(expected = ModbusCommException.class)
    public void readCoil2() {
        this.modbusTcp.readCoil(2001, 1);
    }

    @Test
    public void readDiscreteInput() {
        List<Boolean> booleans = this.modbusTcp.readDiscreteInput(0, 1);
        assertEquals(1, booleans.size());
        assertFalse(booleans.get(0));

        Boolean[] expect = new Boolean[]{false, false, false, false};
        List<Boolean> list = Arrays.asList(false, false, false, false);
        booleans = this.modbusTcp.readDiscreteInput(3, 4);
        assertArrayEquals(expect, booleans.toArray(new Boolean[0]));
    }

    @Test(expected = ModbusCommException.class)
    public void readDiscreteInput1() {
        this.modbusTcp.readDiscreteInput(1, 2001);
    }

    @Test(expected = ModbusCommException.class)
    public void readDiscreteInput2() {
        this.modbusTcp.readDiscreteInput(2001, 1);
    }

    @Test
    public void readHoldRegister() {
        this.modbusTcp.writeHoldRegister(0, 99);
        byte[] expect = new byte[]{0x01, 0x02, 0x03, 0x04};
        this.modbusTcp.writeHoldRegister(0, expect);
        byte[] actual = this.modbusTcp.readHoldRegister(0, 2);
        assertEquals(4, actual.length);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void readInputRegister() {
        byte[] expect = new byte[]{0x00, 0x00, 0x00, 0x00};
        byte[] actual = this.modbusTcp.readInputRegister(0, 2);
        assertEquals(4, actual.length);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void readWriteData() {
        this.modbusTcp.writeInt16(2, (short) 10);
        short data = this.modbusTcp.readInt16(2);
        assertEquals(10, data);

        this.modbusTcp.writeUInt16(3, 20);
        int i = this.modbusTcp.readUInt16(3);
        assertEquals(20, i);

        this.modbusTcp.writeInt32(4, 32);
        int i1 = this.modbusTcp.readInt32(4);
        assertEquals(32, i1);

        this.modbusTcp.writeUInt32(6, 32L);
        long l = this.modbusTcp.readUInt32(6);
        assertEquals(32L, l);

        this.modbusTcp.writeFloat32(8, 12.12f);
        float v = this.modbusTcp.readFloat32(8);
        assertEquals(12.12f, v, 0.0001);

        this.modbusTcp.writeFloat64(10, 33.21);
        double v1 = this.modbusTcp.readFloat64(10);
        assertEquals(33.21, v1, 0.0001);

        this.modbusTcp.writeString(14, "pppp");
        String s = this.modbusTcp.readString(14, 4);
        assertEquals("pppp", s);
    }

    @Test
    public void readWriteData3() {
        this.modbusTcp.writeInt16(1, 2, (short) 10);
        short data = this.modbusTcp.readInt16(1, 2);
        assertEquals(10, data);

        this.modbusTcp.writeUInt16(2, 3, 20);
        int i = this.modbusTcp.readUInt16(2, 3);
        assertEquals(20, i);

        this.modbusTcp.writeInt32(1, 4, 32);
        int i1 = this.modbusTcp.readInt32(1, 4);
        assertEquals(32, i1);

        this.modbusTcp.writeUInt32(2, 6, 32L);
        long l = this.modbusTcp.readUInt32(2, 6);
        assertEquals(32L, l);

        this.modbusTcp.writeFloat32(1, 8, 12.12f);
        float v = this.modbusTcp.readFloat32(1, 8);
        assertEquals(12.12f, v, 0.0001);

        this.modbusTcp.writeFloat64(2, 10, 33.21);
        double v1 = this.modbusTcp.readFloat64(2, 10);
        assertEquals(33.21, v1, 0.0001);

        this.modbusTcp.writeString(1, 14, "pppp");
        String s = this.modbusTcp.readString(1, 14, 4);
        assertEquals("pppp", s);
    }
}