package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class BooleanUtilTest {

    @Test
    public void setBit() {
        byte actual = BooleanUtil.setBit((byte) 0x70, 7, true);
        assertEquals((byte) 0xF0, actual);
        actual = BooleanUtil.setBit((byte) 0x70, 7, true);
        assertEquals((byte) 0xF0, actual);
        actual = BooleanUtil.setBit((byte) 0x30, 6, false);
        assertEquals((byte) 0x30, actual);
        actual = BooleanUtil.setBit((byte) 0x50, 5, false);
        assertEquals((byte) 0x50, actual);
        actual = BooleanUtil.setBit((byte) 0x60, 4, false);
        assertEquals((byte) 0x60, actual);
        actual = BooleanUtil.setBit((byte) 0x78, 3, true);
        assertEquals((byte) 0x78, actual);
        actual = BooleanUtil.setBit((byte) 0x74, 2, true);
        assertEquals((byte) 0x74, actual);
        actual = BooleanUtil.setBit((byte) 0x73, 1, true);
        assertEquals((byte) 0x73, actual);
        actual = BooleanUtil.setBit((byte) 0x71, 0, true);
        assertEquals((byte) 0x71, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setBitException() {
        BooleanUtil.setBit((byte) 0x70, 8, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setBitException1() {
        BooleanUtil.setBit((byte) 0x70, -1, true);
    }

    @Test
    public void getValue() {
        boolean b = BooleanUtil.getValue((byte) 0x80, 7);
        assertTrue(b);
        b = BooleanUtil.getValue((byte) 0x80, 6);
        assertFalse(b);
    }
}