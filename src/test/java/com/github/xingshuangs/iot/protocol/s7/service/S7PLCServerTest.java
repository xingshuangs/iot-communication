package com.github.xingshuangs.iot.protocol.s7.service;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.serializer.DemoBean;
import com.github.xingshuangs.iot.protocol.s7.serializer.DemoLargeBean;
import com.github.xingshuangs.iot.protocol.s7.serializer.S7Serializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class S7PLCServerTest {

    private S7PLCServer server;
    private S7PLC s7PLC;

    @Before
    public void init() {
        this.server = new S7PLCServer();
        server.addDBArea(1, 2, 3, 4);
        this.server.start();
        this.s7PLC = new S7PLC(EPlcType.S1200);
//        this.s7PLC.setPersistence(false);
    }

    @After
    public void destroy() {
        this.server.stop();
        this.s7PLC.close();
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
        bean.setStringData("12345");
        s7Serializer.write(bean);
        bean = s7Serializer.read(DemoBean.class);
        assertTrue(bean.isBitData());
        assertEquals(42767, bean.getUint16Data());
        assertEquals((short) 32767, bean.getInt16Data());
        assertEquals(3147483647L, bean.getUint32Data());
        assertEquals(2147483647, bean.getInt32Data());
        assertEquals(3.14f, bean.getFloat32Data(), 0.001);
        assertEquals(4.15, bean.getFloat64Data(), 0.001);
        assertArrayEquals(bytes, bean.getByteData());
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
}