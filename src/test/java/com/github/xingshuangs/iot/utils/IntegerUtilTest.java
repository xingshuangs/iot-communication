package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class IntegerUtilTest {

    @Test
    public void toByteArray() {
        byte[] actual = IntegerUtil.toByteArray(2111286);
        byte[] expect = new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray1() {
        byte[] actual = IntegerUtil.toByteArray(2111286, true);
        byte[] expect = new byte[]{(byte) 0x36, (byte) 0x37, (byte) 0x20, (byte) 0x00};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toInt32() {
        byte[] data = new byte[]{(byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        int actual = IntegerUtil.toInt32(data);
        assertEquals(2147483647, actual);
        data = new byte[]{(byte) 0x00, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1};
        actual = IntegerUtil.toInt32(data, 0, false);
        assertEquals(3881889, actual);
        data = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        actual = IntegerUtil.toInt32(data, 0, false);
        assertEquals(-1, actual);
        data = new byte[]{(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        actual = IntegerUtil.toInt32(data, 0, false);
        assertEquals(-2147483648, actual);
        data = new byte[]{(byte) 0x8F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        actual = IntegerUtil.toInt32(data, 0, false);
        assertEquals(-1879048193, actual);

        data = new byte[]{(byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1};
        actual = IntegerUtil.toInt32(data, 0, true);
        assertEquals(-1589953761, actual);
    }

    @Test
    public void toUInt32() {
        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        long actual = IntegerUtil.toUInt32(data);
        assertEquals(4294967295L, actual);
        data = new byte[]{(byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1};
        actual = IntegerUtil.toUInt32(data, 0, false);
        assertEquals(523975585L, actual);

        data = new byte[]{(byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1};
        actual = IntegerUtil.toUInt32(data, 0, true);
        assertEquals(2705013535L, actual);
    }
}