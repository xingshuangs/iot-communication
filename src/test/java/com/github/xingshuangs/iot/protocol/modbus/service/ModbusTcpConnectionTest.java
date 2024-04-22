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


import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author xingshuang
 */
@Ignore
@Slf4j
public class ModbusTcpConnectionTest {

    private final ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");

    @Before
    public void before() {
        this.plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        this.plc.setPersistence(false);
    }

    @Test
    public void readWriteData() {
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
        assertEquals(10, data);

        plc.writeUInt16(3, 20);
        int i = plc.readUInt16(3);
        assertEquals(20, i);

        plc.writeInt32(4, 32);
        int i1 = plc.readInt32(4);
        assertEquals(32, i1);

        plc.writeUInt32(6, 32L);
        long l = plc.readUInt32(6);
        assertEquals(32L, l);

        plc.writeFloat32(8, 12.12f);
        float v = plc.readFloat32(8);
        assertEquals(12.12f, v, 0.0001);

        plc.writeFloat64(10, 33.21);
        double v1 = plc.readFloat64(10);
        assertEquals(33.21, v1, 0.0001);

        plc.writeString(14, "pppp");
        String s = plc.readString(14, 4);
        assertEquals("pppp", s);
    }
}
