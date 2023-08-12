package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class LRCUtilTest {

    @Test
    public void lrcTest() {
        byte[] src = new byte[]{0x01, 0x03, 0x00, 0x00, 0x00, 0x14};
        byte actual = LRCUtil.lrc(src);
        assertEquals((byte) 0xE8, actual);
    }

    @Test
    public void lrcTest1() {
        byte[] src = new byte[]{0x01, 0x03, 0x00, 0x00, 0x00, 0x14};
        boolean actual = LRCUtil.lrc(src, (byte) 0xE8);
        assertTrue(actual);
    }
}