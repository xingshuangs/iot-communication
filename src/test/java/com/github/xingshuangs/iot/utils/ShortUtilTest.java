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
}