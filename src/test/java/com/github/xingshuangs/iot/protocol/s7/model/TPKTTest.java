package com.github.xingshuangs.iot.protocol.s7.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class TPKTTest {

    @Test
    public void byteArrayLength() {
        TPKT tpkt = new TPKT();
        assertEquals(4, tpkt.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        TPKT tpkt = new TPKT();
        tpkt.setLength(100);
        byte[] actual = tpkt.toByteArray();
        byte[] expect = {(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x64};
        assertArrayEquals(expect, actual);
    }
}