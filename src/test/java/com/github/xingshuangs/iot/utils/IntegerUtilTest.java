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
}