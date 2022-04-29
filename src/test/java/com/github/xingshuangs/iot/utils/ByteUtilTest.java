package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class ByteUtilTest {

    @Test
    public void toByte() {
        byte actual = ByteUtil.toByte(11);
        assertEquals((byte) 0x0B, actual);
    }

    @Test
    public void toUInt8() {
        int actual = ByteUtil.toUInt8((byte) 0xFF);
        assertEquals(255, actual);
    }
}