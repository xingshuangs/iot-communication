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

    @Test
    public void toStr() {
        byte[] data = {(byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};
        String actual = ByteUtil.toStr(data);
        assertEquals("P_PROGRAM", actual);
    }
}