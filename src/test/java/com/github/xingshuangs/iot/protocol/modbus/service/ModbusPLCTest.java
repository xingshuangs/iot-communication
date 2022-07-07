package com.github.xingshuangs.iot.protocol.modbus.service;

import com.github.xingshuangs.iot.utils.ShortUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class ModbusPLCTest {

    private ModbusPLC plc = new ModbusPLC(1);

    @Test
    public void readCoil() {
        List<Boolean> booleans = plc.readCoil(0, 1);
        assertEquals(1, booleans.size());
        assertArrayEquals(new Boolean[]{true}, booleans.toArray(new Boolean[0]));

        booleans = plc.readCoil(0, 4);
        assertArrayEquals(new Boolean[]{true, true, false, false}, booleans.toArray(new Boolean[0]));
    }

    @Test
    public void writeCoil() {
        plc.writeCoil(0, true);

        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        list.add(true);
        list.add(false);
        plc.writeCoil(0, list);
    }

    @Test
    public void readDiscreteInput() {
        List<Boolean> booleans = plc.readDiscreteInput(0, 4);
        assertEquals(4, booleans.size());
        assertArrayEquals(new Boolean[]{true, true, true, false}, booleans.toArray(new Boolean[0]));
    }

    @Test
    public void readInputRegister() {
        byte[] bytes = plc.readInputRegister(0, 2);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x21, (byte) 0x00, (byte) 0x00}, bytes);
    }

    @Test
    public void readHoldRegister() {
        byte[] bytes = plc.readHoldRegister(0, 1);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x33}, bytes);
        int actual = ShortUtil.toUInt16(bytes);
        assertEquals(51, actual);

        bytes = plc.readHoldRegister(0, 2);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x33, (byte) 0x00, (byte) 0x21}, bytes);

        bytes = plc.readHoldRegister(0, 4);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x33, (byte) 0x00, (byte) 0x21,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00}, bytes);
    }

    @Test
    public void writeHoldRegister() {
        plc.writeHoldRegister(2, 123);

        List<Integer> list = new ArrayList<>();
        list.add(11);
        list.add(12);
        list.add(13);
        list.add(14);
        plc.writeHoldRegister(3, list);
    }


    @Test
    public void readWriteData() {
        plc.writeInt16(4, (short) 10);
        short data = plc.readInt16(4);
        assertEquals(10, data);

        plc.writeUInt16(4, 20);
        int i = plc.readUInt16(4);
        assertEquals(20, i);

        plc.writeInt32(4, 32);
        int i1 = plc.readInt32(4);
        assertEquals(32, i1);

        plc.writeUInt32(4, 32L);
        long l = plc.readUInt32(4);
        assertEquals(32L, l);

        plc.writeFloat32(4, 12.12f);
        float v = plc.readFloat32(4);
        assertEquals(12.12f, v, 0.0001);

        plc.writeFloat64(4, 33.21);
        double v1 = plc.readFloat64(4);
        assertEquals(33.21, v1, 0.0001);

        plc.writeString(4, "pppp");
        String s = plc.readString(4, 4);
        assertEquals("pppp", s);
    }
}