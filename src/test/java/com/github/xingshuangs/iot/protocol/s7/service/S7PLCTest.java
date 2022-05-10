package com.github.xingshuangs.iot.protocol.s7.service;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.Assert.*;

//@Ignore
public class S7PLCTest {
    private S7PLC s7PLC = new S7PLC(EPlcType.S1200,"192.168.3.98");

    @Test
    public void readMultiByte() throws IOException {
        MultiAddressRead addressRead = new MultiAddressRead();
        addressRead.addData("DB1.0", 1)
                .addData("DB1.2", 3)
                .addData("DB1.3", 5);
        List<byte[]> list = s7PLC.readMultiByte(addressRead);
        assertEquals(1, list.get(0).length);
        assertEquals(3, list.get(1).length);
        assertEquals(5, list.get(2).length);
    }

    @Test
    public void readByte() throws IOException {
        byte[] actual = s7PLC.readByte("DB14.2", 3);
        assertEquals(3, actual.length);
        byte b = s7PLC.readByte("DB1.1");
    }

    @Test
    public void readBoolean() throws IOException {
        List<Boolean> actual = s7PLC.readBoolean("DB1.2.0", "DB1.2.1", "DB1.2.7");
        assertEquals(3, actual.size());
    }

    @Test
    public void readUInt16() throws IOException {
        int i = s7PLC.readUInt16("DB1.0");
        System.out.println(i);
        List<Integer> actual = s7PLC.readUInt16("DB1.0", "DB1.2");
        assertEquals(2, actual.size());
    }

    @Test
    public void readUInt32() throws IOException {
        long l = s7PLC.readUInt32("DB1.0");
        System.out.println(l);
        List<Long> actual = s7PLC.readUInt32("DB1.0", "DB1.4");
        assertEquals(2, actual.size());
    }


    @Test
    public void readFloat32() throws IOException {
        float actual = s7PLC.readFloat32("DB1.0");
        assertEquals(12, actual, 0.00001);

        List<Float> floats = s7PLC.readFloat32("DB1.0", "DB1.4");
        assertEquals(12, floats.get(0), 0.0001);
        assertEquals(33, floats.get(1), 0.0001);
    }

    @Test
    public void readFloat64() throws IOException {
        double actual = s7PLC.readFloat64("DB1.0");
        assertEquals(33, actual, 0.00001);
    }

    @Test
    public void writeBoolean() throws IOException {
        s7PLC.writeBoolean("DB2.0.7", true);
    }

    @Test
    public void writeByte() throws IOException {
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        byte actual = s7PLC.readByte("DB2.1");
        assertEquals((byte) 0x11, actual);
    }

    @Test
    public void writeUInt16() throws IOException {
        s7PLC.writeUInt16("DB2.0", 0x2222);
        int actual = s7PLC.readUInt16("DB2.0");
        assertEquals(0x2222, actual);
    }

    @Test
    public void writeInt16() throws IOException {
        s7PLC.writeInt16("DB2.0", (short) 0x1111);
    }

    @Test
    public void writeUInt32() throws IOException {
        s7PLC.writeUInt32("DB2.0", 0x11111122);
        long actual = s7PLC.readUInt32("DB2.0");
        assertEquals(0x11111122L, actual);
    }

    @Test
    public void writeInt32() throws IOException {
        s7PLC.writeInt32("DB2.0", 0x11113322);
    }

    @Test
    public void writeFloat32() throws IOException {
        s7PLC.writeFloat32("DB2.0", 12);
        float actual = s7PLC.readFloat32("DB2.0");
        assertEquals(12, actual, 0.00001);
    }

    @Test
    public void writeFloat64() throws IOException {
        s7PLC.writeFloat64("DB2.0", 12.02);
        double actual = s7PLC.readFloat64("DB2.0");
        assertEquals(12.02, actual, 0.00001);
    }

    @Test
    public void writeMultiData() throws IOException {
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addByte("DB2.0", (byte) 0x11);
        addressWrite.addUInt16("DB2.2", 88);
        addressWrite.addBoolean("DB2.1.0", true);
        s7PLC.writeMultiData(addressWrite);
        boolean actual = s7PLC.readBoolean("DB2.1.0");
        assertTrue(actual);
    }
}