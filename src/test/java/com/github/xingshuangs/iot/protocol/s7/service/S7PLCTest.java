package com.github.xingshuangs.iot.protocol.s7.service;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class S7PLCTest {
    private S7PLC s7PLC = new S7PLC(EPlcType.S1200, "192.168.3.98");
//    private S7PLC s7PLC = new S7PLC(EPlcType.S1200);

    @Test
    public void readMultiByte() {
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
    public void readByte() {
        byte[] actual = s7PLC.readByte("DB14.0.1", 2);
        assertEquals(2, actual.length);
//        byte b = s7PLC.readByte("DB1.1");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            try {
                s7PLC.readBoolean("DB14.2.0");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void readBoolean() {
        List<Boolean> actual = s7PLC.readBoolean("DB1.2.0", "DB1.2.1", "DB1.2.7");
        assertEquals(3, actual.size());
    }

    @Test
    public void readUInt16() {
        int i = s7PLC.readUInt16("DB14.0");
        System.out.println(i);
        List<Integer> actual = s7PLC.readUInt16("DB1.0", "DB1.2");
        assertEquals(2, actual.size());
    }

    @Test
    public void readUInt32() {
        long l = s7PLC.readUInt32("DB1.0");
        System.out.println(l);
        List<Long> actual = s7PLC.readUInt32("DB1.0", "DB1.4");
        assertEquals(2, actual.size());
    }


    @Test
    public void readFloat32() {
        float actual = s7PLC.readFloat32("DB1.0");
        assertEquals(12, actual, 0.00001);

        List<Float> floats = s7PLC.readFloat32("DB1.0", "DB1.4");
        assertEquals(12, floats.get(0), 0.0001);
        assertEquals(33, floats.get(1), 0.0001);
    }

    @Test
    public void readFloat64() {
        double actual = s7PLC.readFloat64("DB1.0");
        assertEquals(33, actual, 0.00001);
    }

    @Test
    public void readString() {
        String str = s7PLC.readString("DB14.4");
        assertEquals("hello", str);
    }

    @Test
    public void writeString() {
        s7PLC.writeString("DB14.4","demo");
        String str = s7PLC.readString("DB14.4");
        assertEquals("demo", str);
    }

    @Test
    public void writeBoolean() {
        s7PLC.writeBoolean("DB2.0.7", true);
    }

    @Test
    public void writeByte() {
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        byte actual = s7PLC.readByte("DB2.1");
        assertEquals((byte) 0x11, actual);
    }

    @Test
    public void writeUInt16() {
        s7PLC.writeUInt16("DB2.0", 0x2222);
        int actual = s7PLC.readUInt16("DB2.0");
        assertEquals(0x2222, actual);
    }

    @Test
    public void writeInt16() {
        s7PLC.writeInt16("DB14.0", (short) 15);
    }

    @Test
    public void writeUInt32() {
        s7PLC.writeUInt32("DB2.0", 0x11111122);
        long actual = s7PLC.readUInt32("DB2.0");
        assertEquals(0x11111122L, actual);
    }

    @Test
    public void writeInt32() {
        s7PLC.writeInt32("DB2.0", 0x11113322);
    }

    @Test
    public void writeFloat32() {
        s7PLC.writeFloat32("DB2.0", 12);
        float actual = s7PLC.readFloat32("DB2.0");
        assertEquals(12, actual, 0.00001);
    }

    @Test
    public void writeFloat64() {
        s7PLC.writeFloat64("DB2.0", 12.02);
        double actual = s7PLC.readFloat64("DB2.0");
        assertEquals(12.02, actual, 0.00001);
    }

    @Test
    public void writeMultiData() {
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addByte("DB2.0", (byte) 0x11)
                .addUInt16("DB2.2", 88)
                .addBoolean("DB2.1.0", true);
        s7PLC.writeMultiData(addressWrite);
        boolean actual = s7PLC.readBoolean("DB2.1.0");
        assertTrue(actual);
    }

    @Test
    public void read() {
        boolean b = s7PLC.readBoolean("M1.3");
        assertFalse(b);
        b = s7PLC.readBoolean("M1.2");
        assertTrue(b);
        b = s7PLC.readBoolean("Q0.1");
        assertTrue(b);
        byte q0 = s7PLC.readByte("Q0");
        assertEquals((byte) 0x03, q0);
        byte m1 = s7PLC.readByte("M1");
        System.out.println(m1);
    }

    @Test
    public void readI() {
        List<Boolean> booleans = s7PLC.readBoolean("I0.0", "I0.1", "I0.2", "I0.3", "I0.4", "I0.5");
        System.out.println(booleans);
    }

    @Test
    public void writeI() {
        s7PLC.writeBoolean("I0.5", true);
        System.out.println("结果");
    }

    @Test
    public void readQ() {
        List<Boolean> booleans = s7PLC.readBoolean("Q0.0", "Q0.1", "Q0.2", "Q0.3", "Q0.4", "Q0.5", "Q0.6", "Q0.7", "Q1.0", "Q1.1");
        System.out.println(booleans);
    }

    @Test
    public void writeQ() {
        s7PLC.writeBoolean("Q0.7", true);
        System.out.println();
    }

    @Test
    public void readM() {
        List<Boolean> booleans = s7PLC.readBoolean("M1.0", "M1.1", "M1.2", "M1.3", "M1.4", "M1.5", "M1.6", "M1.7");
        System.out.println(booleans);
    }

    @Test
    public void writeM() {
        s7PLC.writeBoolean("M1.4", true);
        System.out.println();
    }
}