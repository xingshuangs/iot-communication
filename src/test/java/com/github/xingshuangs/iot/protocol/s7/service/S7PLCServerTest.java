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

package com.github.xingshuangs.iot.protocol.s7.service;

import com.github.xingshuangs.iot.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.serializer.DemoBean;
import com.github.xingshuangs.iot.protocol.s7.serializer.DemoLargeBean;
import com.github.xingshuangs.iot.protocol.s7.serializer.S7Parameter;
import com.github.xingshuangs.iot.protocol.s7.serializer.S7Serializer;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@Slf4j
public class S7PLCServerTest {

    private S7PLCServer server;
    private S7PLC s7PLC;

    @Before
    public void init() {
        this.server = new S7PLCServer();
        server.addDBArea(1, 2, 3, 4);
        this.server.start(8888);
        this.s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1", 8888);
        this.s7PLC.setComCallback(x -> log.debug("[{}] {}", x.length, HexUtil.toHexString(x)));
//        this.s7PLC.setPersistence(false);
//        this.s7PLC.connect();
    }

    @After
    public void destroy() {
        this.s7PLC.close();
        this.server.stop();
    }

    @Test
    public void addDBAreaTest() {
        Set<String> areas = server.getAvailableAreas();
        assertEquals(9, areas.size());
        server.addDBArea(20);
        areas = server.getAvailableAreas();
        assertEquals(10, areas.size());
    }

    @Test
    public void readWriteByteTest() {
        this.s7PLC.writeByte("DB1.0", (byte) 0x01);
        byte b = this.s7PLC.readByte("DB1.0");
        assertEquals((byte) 0x01, b);
    }

    @Test
    public void multiReadWriteByteTest() {
        this.s7PLC.writeInt16("DB1.0", (short) 22);
        this.s7PLC.writeInt16("DB1.2", (short) 33);
        List<Short> shorts = this.s7PLC.readInt16("DB1.0", "DB1.2");
        assertEquals((short) 22, shorts.get(0).shortValue());
        assertEquals((short) 33, shorts.get(1).shortValue());
    }

    @Test
    public void serializerTest() {
        S7Serializer s7Serializer = S7Serializer.newInstance(this.s7PLC);
        byte[] bytes = {(byte) 0x01, (byte) 0x02, (byte) 0x03};
        DemoBean bean = new DemoBean();
        bean.setBitData(true);
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        bean.setByteData(bytes);
        bean.setStringData("天气好");
        bean.setTimeData(12L);
        bean.setDateData(LocalDate.of(2023, 5, 15));
        bean.setTimeOfDayData(LocalTime.of(20, 22, 13));
        bean.setDateTimeData(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555));
        s7Serializer.write(bean);
        bean = s7Serializer.read(DemoBean.class);
        assertTrue(bean.getBitData());
        assertEquals(42767, bean.getUint16Data().intValue());
        assertEquals((short) 32767, bean.getInt16Data().intValue());
        assertEquals(3147483647L, bean.getUint32Data().longValue());
        assertEquals(2147483647, bean.getInt32Data().intValue());
        assertEquals(3.14f, bean.getFloat32Data(), 0.001);
        assertEquals(4.15, bean.getFloat64Data(), 0.001);
        assertArrayEquals(bytes, bean.getByteData());
        assertEquals("天气好", bean.getStringData());
        assertEquals(12, bean.getTimeData().longValue());
        assertEquals(LocalDate.of(2023, 5, 15), bean.getDateData());
        assertEquals(LocalTime.of(20, 22, 13), bean.getTimeOfDayData());
        assertEquals(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555), bean.getDateTimeData());
    }

    @Test
    public void serializerTest1() {
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        byte[] byteData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        List<S7Parameter> list = new ArrayList<>();
        list.add(new S7Parameter("DB1.0.1", EDataType.BOOL, 1, true));
        list.add(new S7Parameter("DB1.4", EDataType.UINT16, 1, 42767));
        list.add(new S7Parameter("DB1.6", EDataType.INT16, 1, (short) 32767));
        list.add(new S7Parameter("DB1.8", EDataType.UINT32, 1, 3147483647L));
        list.add(new S7Parameter("DB1.12", EDataType.INT32, 1, 2147483647));
        list.add(new S7Parameter("DB1.16", EDataType.FLOAT32, 1, 3.14f));
        list.add(new S7Parameter("DB1.20", EDataType.FLOAT64, 1, 4.15));
        list.add(new S7Parameter("DB1.28", EDataType.BYTE, 3, byteData));
        list.add(new S7Parameter("DB1.31", EDataType.STRING, 10, "1234567890"));
        list.add(new S7Parameter("DB1.43", EDataType.TIME, 1, 12L));
        list.add(new S7Parameter("DB1.47", EDataType.DATE, 1, LocalDate.of(2023, 5, 15)));
        list.add(new S7Parameter("DB1.49", EDataType.TIME_OF_DAY, 1, LocalTime.of(20, 22, 13)));
        list.add(new S7Parameter("DB1.53", EDataType.DTL, 1, LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555)));
        s7Serializer.write(list);

        list = new ArrayList<>();
        list.add(new S7Parameter("DB1.0.1", EDataType.BOOL));
        list.add(new S7Parameter("DB1.4", EDataType.UINT16));
        list.add(new S7Parameter("DB1.6", EDataType.INT16));
        list.add(new S7Parameter("DB1.8", EDataType.UINT32));
        list.add(new S7Parameter("DB1.12", EDataType.INT32));
        list.add(new S7Parameter("DB1.16", EDataType.FLOAT32));
        list.add(new S7Parameter("DB1.20", EDataType.FLOAT64));
        list.add(new S7Parameter("DB1.28", EDataType.BYTE, 3));
        list.add(new S7Parameter("DB1.31", EDataType.STRING, 10));
        list.add(new S7Parameter("DB1.43", EDataType.TIME));
        list.add(new S7Parameter("DB1.47", EDataType.DATE));
        list.add(new S7Parameter("DB1.49", EDataType.TIME_OF_DAY));
        list.add(new S7Parameter("DB1.53", EDataType.DTL));
        List<S7Parameter> actual = s7Serializer.read(list);

        assertTrue((boolean) actual.get(0).getValue());
        assertEquals(42767, (int) actual.get(1).getValue());
        assertEquals(32767, (short) actual.get(2).getValue());
        assertEquals(3147483647L, (long) actual.get(3).getValue());
        assertEquals(2147483647, (int) actual.get(4).getValue());
        assertEquals(3.14f, (float) actual.get(5).getValue(), 0.001);
        assertEquals(4.15, (double) actual.get(6).getValue(), 0.001);
        assertArrayEquals(byteData, (byte[]) actual.get(7).getValue());
        assertEquals("1234567890", actual.get(8).getValue());
        assertEquals(12, (long) actual.get(9).getValue());
        assertEquals(LocalDate.of(2023, 5, 15), actual.get(10).getValue());
        assertEquals(LocalTime.of(20, 22, 13), actual.get(11).getValue());
        assertEquals(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555), actual.get(12).getValue());
    }

    @Test
    public void writeLargeData() {
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoLargeBean bean = s7Serializer.read(DemoLargeBean.class);
        System.out.println("-------------------------------");
        bean.getByteData2()[0] = (byte) 0x05;
        bean.getByteData3()[0] = (byte) 0x05;
        bean.getByteData4()[0] = (byte) 0x05;
        bean.getByteData5()[0] = (byte) 0x05;
        bean.getByteData6()[0] = (byte) 0x05;
        bean.getByteData7()[0] = (byte) 0x05;
        bean.getByteData2()[64] = (byte) 0x02;
        bean.getByteData3()[199] = (byte) 0x03;
        bean.getByteData4()[321] = (byte) 0x04;
        bean.getByteData5()[98] = (byte) 0x05;
        bean.getByteData6()[499] = (byte) 0x06;
        bean.getByteData7()[43] = (byte) 0x07;
        s7Serializer.write(bean);
    }

    @Test
    public void writeStringTest() {
        this.s7PLC.writeString("DB1.10", "123456");
        String actual = this.s7PLC.readString("DB1.10", 6);
        assertEquals("123456", actual);
    }

    @Test
    public void readMultiAddress18() {
        List<String> str = new ArrayList<>();
        str.add("DB1.1");
        str.add("DB1.2");
        str.add("DB1.3");
        str.add("DB1.4");
        str.add("DB1.5");
        str.add("DB1.6");
        str.add("DB1.7");
        str.add("DB1.8");
        str.add("DB1.9");
        str.add("DB1.10");
        str.add("DB1.11");
        str.add("DB1.12");
        str.add("DB1.13");
        str.add("DB1.14");
        str.add("DB1.15");
        str.add("DB1.16");
        str.add("DB1.17");
        str.add("DB1.18");
        List<Integer> actual = s7PLC.readUInt16(str);
        assertEquals(18, actual.size());
    }

    @Test(expected = Test.None.class)
    public void writeMultiAddress12() {
        MultiAddressWrite write = new MultiAddressWrite();
        write.addUInt16("DB1.1", 1);
        write.addUInt16("DB1.2", 1);
        write.addUInt16("DB1.3", 1);
        write.addUInt16("DB1.4", 1);
        write.addUInt16("DB1.5", 1);
        write.addUInt16("DB1.6", 1);
        write.addUInt16("DB1.7", 1);
        write.addUInt16("DB1.8", 1);
        write.addUInt16("DB1.9", 1);
        write.addUInt16("DB1.10", 1);
        write.addUInt16("DB1.11", 1);
        write.addUInt16("DB1.12", 1);
        s7PLC.writeMultiData(write);
    }
}