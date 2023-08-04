package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class CRCUtilTest {

    @Test
    public void crc16ToByteArray() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A};
        byte[] expect = new byte[]{ (byte) 0xC5,(byte) 0xCD};
        byte[] actual = CRCUtil.crc16ToByteArray(data);
        assertArrayEquals(expect, actual);
        data = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x14, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        expect = new byte[]{(byte) 0x1E, (byte) 0x1C};
        actual = CRCUtil.crc16ToByteArray(data);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void crc16ToByteArray1() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A};
        byte[] expect =new byte[]{ (byte) 0xC5,(byte) 0xCD};
        boolean actual = CRCUtil.crc16(data, expect);
        assertTrue(actual);
    }

    @Test
    public void crc16ToInt() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A};
        boolean actual = CRCUtil.crc16(data, 50637);
        assertTrue(actual);
    }
}