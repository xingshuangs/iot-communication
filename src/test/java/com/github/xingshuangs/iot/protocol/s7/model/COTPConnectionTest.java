package com.github.xingshuangs.iot.protocol.s7.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class COTPConnectionTest {

    @Test
    public void byteArrayLength() {
        COTPConnection connection = new COTPConnection();
        assertEquals(18, connection.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        COTPConnection connection = COTPConnection.crConnectRequest(0x0100,0x0100);
        byte[] actual = connection.toByteArray();
        byte[] expect = {(byte) 0x11, (byte) 0xE0,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00,
                (byte) 0xC0,
                (byte) 0x01,
                (byte) 0x0A,
                (byte) 0xC1,
                (byte) 0x02,
                (byte) 0x01, (byte) 0x00,
                (byte) 0xC2,
                (byte) 0x02,
                (byte) 0x01, (byte) 0x00};

        assertArrayEquals(expect, actual);
    }
}