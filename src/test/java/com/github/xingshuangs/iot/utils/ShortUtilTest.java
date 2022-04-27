package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class ShortUtilTest {

    @Test
    public void toByteArray() {
        byte[] actual = ShortUtil.toByteArray((short) 24565);
        byte[] expect = new byte[]{(byte) 0x5F, (byte) 0xF5};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray1() {
        byte[] actual = ShortUtil.toByteArray((short) 24565, true);
        byte[] expect = new byte[]{(byte) 0xF5, (byte) 0x5F};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toInt16() {
        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xFF};
        short actual = ShortUtil.toInt16(data);
        assertEquals(-1, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toInt16(data);
        assertEquals(32767, actual);

        data = new byte[]{(byte) 0xFF, (byte) 0x7F};
        actual = ShortUtil.toInt16(data, 0, true);
        assertEquals(32767, actual);
    }

    @Test
    public void toUInt16() {
        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xFF};
        int actual = ShortUtil.toUInt16(data);
        assertEquals(65535, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toUInt16(data);
        assertEquals(32767, actual);

        data = new byte[]{(byte) 0x7F, (byte) 0xFF};
        actual = ShortUtil.toUInt16(data, 0, true);
        assertEquals(65407, actual);
    }
}