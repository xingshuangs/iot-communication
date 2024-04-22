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

package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@Ignore
public class S7SerializerTest {
    private final S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");

    @Test
    public void read() {
        s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
//        s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoBean bean = s7Serializer.read(DemoBean.class);
        log.info(bean.toString());
    }

    @Test
    public void write() {
        s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        byte[] byteData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        DemoBean bean = new DemoBean();
        bean.setBitData(true);
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        bean.setByteData(byteData);
        bean.setStringData("123456789");
        bean.setTimeData(12L);
        bean.setDateData(LocalDate.of(2023, 5, 15));
        bean.setTimeOfDayData(LocalTime.of(20, 22, 13));
        bean.setDateTimeData(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555));
        s7Serializer.write(bean);
        DemoBean actual = s7Serializer.read(DemoBean.class);
        assertTrue(actual.getBitData());
        assertEquals(42767, actual.getUint16Data().intValue());
        assertEquals(32767, actual.getInt16Data().intValue());
        assertEquals(3147483647L, actual.getUint32Data().longValue());
        assertEquals(2147483647, actual.getInt32Data().intValue());
        assertEquals(3.14f, actual.getFloat32Data(), 0.001);
        assertEquals(4.15, actual.getFloat64Data(), 0.001);
        assertArrayEquals(byteData, actual.getByteData());
        assertEquals("123456789", actual.getStringData());
        assertEquals(12, actual.getTimeData().longValue());
        assertEquals(LocalDate.of(2023, 5, 15), actual.getDateData());
        assertEquals(LocalTime.of(20, 22, 13), actual.getTimeOfDayData());
        assertEquals(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555), actual.getDateTimeData());
    }

    @Test
    public void writeLargeData() {
        s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoLargeBean bean = s7Serializer.read(DemoLargeBean.class);
        System.out.println("-------------------------------");
        bean.setBitData(true);
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
    public void dbData() {
        s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DB80 bean = s7Serializer.read(DB80.class);
        System.out.println(bean);
    }

    @Test
    public void readTest1() {
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
}