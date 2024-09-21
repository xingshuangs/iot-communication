package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class LongUtilTest {

    @Test
    public void toByteArray() {
        byte[] actual = LongUtil.toByteArray(1537229996552836696L);
        byte[] expect = new byte[]{(byte) 0x15, (byte) 0x55, (byte) 0x56, (byte) 0x89, (byte) 0x8A, (byte) 0x9B, (byte) 0x56, (byte) 0x58};
        assertArrayEquals(expect, actual);

        expect = new byte[]{(byte) 0x58, (byte) 0x56, (byte) 0x9B, (byte) 0x8A, (byte) 0x89, (byte) 0x56, (byte) 0x55, (byte) 0x15};
        actual = LongUtil.toByteArray(1537229996552836696L, true);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toInt64() {
        byte[] src = new byte[]{(byte) 0x15, (byte) 0x55, (byte) 0x56, (byte) 0x89, (byte) 0x8A, (byte) 0x9B, (byte) 0x56, (byte) 0x58};
        long expect = 1537229996552836696L;
        long actual = LongUtil.toInt64(src, 0, false);
        assertEquals(expect, actual);
        src = new byte[]{(byte) 0x58, (byte) 0x56, (byte) 0x9B, (byte) 0x8A, (byte) 0x89, (byte) 0x56, (byte) 0x55, (byte) 0x15};
        actual = LongUtil.toInt64(src, 0, true);
        assertEquals(expect, actual);
    }
}