package com.github.xingshuangs.iot.protocol.common.buff;

import org.junit.Test;

import static org.junit.Assert.*;


public class ByteReadBuffTest {

    @Test
    public void getBoolean() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55});
        boolean b1 = buff.getBoolean(0);
        assertTrue(b1);
        boolean b2 = buff.getBoolean(0, 1);
        assertFalse(b2);
    }

    @Test
    public void getByte() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55});
        byte actual = buff.getByte();
        assertEquals((byte) 0x55, actual);
    }

    @Test
    public void getBytes() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55, (byte) 0x33, (byte) 0x22});
        buff.getByte();
        byte[] actual = buff.getBytes(2);
        assertArrayEquals(new byte[]{(byte) 0x33, (byte) 0x22}, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x55, (byte) 0x33, (byte) 0x22});
        buff.getByte();
        actual = buff.getBytes();
        assertArrayEquals(new byte[]{(byte) 0x33, (byte) 0x22}, actual);
    }

    @Test
    public void getByteToInt() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55});
        int actual = buff.getByteToInt();
        assertEquals(85, actual);
    }

    @Test
    public void getInt16() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x5F, (byte) 0xF5});
        short actual = buff.getInt16();
        assertEquals(24565, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0xF5, (byte) 0x5F}, true);
        actual = buff.getInt16();
        assertEquals(24565, actual);
    }

    @Test
    public void getUInt16() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x5F, (byte) 0xF5});
        int actual = buff.getUInt16();
        assertEquals(24565, actual);
    }

    @Test
    public void getInt32() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36});
        int actual = buff.getInt32();
        assertEquals(2111286, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x36, (byte) 0x37, (byte) 0x20, (byte) 0x00}, true);
        actual = buff.getInt32();
        assertEquals(2111286, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x2A, (byte) 0xF8}, EByteBuffFormat.DC_BA);
        actual = buff.getInt32();
        assertEquals(11000, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x2A, (byte) 0xF8, (byte) 0x00, (byte) 0x00}, EByteBuffFormat.BA_DC);
        actual = buff.getInt32();
        assertEquals(11000, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xF8, (byte) 0x2A}, EByteBuffFormat.CD_AB);
        actual = buff.getInt32();
        assertEquals(11000, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0xF8, (byte) 0x2A, (byte) 0x00, (byte) 0x00}, EByteBuffFormat.AB_CD);
        actual = buff.getInt32();
        assertEquals(11000, actual);
    }

    @Test
    public void getUInt32() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36});
        long actual = buff.getUInt32();
        assertEquals(2111286L, actual);
    }

    @Test
    public void getFloat32() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7});
        float actual = buff.getFloat32();
        assertEquals(33.16f, actual, 0.001);

        buff = new ByteReadBuff(new byte[]{(byte) 0xD7, (byte) 0xA3, (byte) 0x04, (byte) 0x42}, true);
        actual = buff.getFloat32();
        assertEquals(33.16f, actual, 0.001);
    }

    @Test
    public void getFloat64() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7});
        double actual = buff.getFloat64();
        assertEquals(156665.35455556, actual, 0.001);

        buff = new ByteReadBuff(new byte[]{(byte) 0x03, (byte) 0x41, (byte) 0xCA, (byte) 0x1F, (byte) 0x21, (byte) 0xD6, (byte) 0xB7, (byte) 0x39}, EByteBuffFormat.CD_AB);
        actual = buff.getFloat64();
        assertEquals(156665.35455556, actual, 0.001);

        buff = new ByteReadBuff(new byte[]{(byte) 0x39, (byte) 0xB7, (byte) 0xD6, (byte) 0x21, (byte) 0x1F, (byte) 0xCA, (byte) 0x41, (byte) 0x03}, EByteBuffFormat.BA_DC);
        actual = buff.getFloat64();
        assertEquals(156665.35455556, actual, 0.001);

        buff = new ByteReadBuff(new byte[]{(byte) 0xB7, (byte) 0x39, (byte) 0x21, (byte) 0xD6, (byte) 0xCA, (byte) 0x1F, (byte) 0x03, (byte) 0x41}, EByteBuffFormat.AB_CD);
        actual = buff.getFloat64();
        assertEquals(156665.35455556, actual, 0.001);
    }

    @Test
    public void getString() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33});
        String actual = buff.getString(4);
        assertEquals("0123", actual);
    }

    @Test
    public void getBitToInt() {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33});
        assertEquals(1, buff.getBitToInt(0, 4));
        assertEquals(1, buff.getBitToInt(0, 5));
        assertEquals(0, buff.getBitToInt(0, 6));
    }

    @Test
    public void getByteToInt1(){
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33});
        assertEquals(3, buff.getByteToInt(0, 4,2));
        assertEquals(6, buff.getByteToInt(0, 3,3));
        assertEquals(16, buff.getByteToInt(0, 0,5));
    }
}