package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import org.junit.Test;

import static org.junit.Assert.*;


public class COTPDataTest {

    @Test
    public void byteArrayLength() {
        COTPData cotpData = new COTPData();
        assertEquals(3, cotpData.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        COTPData cotpData = new COTPData();
        cotpData.setLength((byte) 0x11);
        cotpData.setPduType(EPduType.CONNECT_REQUEST);
        cotpData.setTpduNumber((byte) 0x01);
        cotpData.setLastDataUnit(true);
        byte[] actual = cotpData.toByteArray();
        byte[] expect = {(byte) 0x11, (byte)0xE0, (byte)0x81};
        assertArrayEquals(expect, actual);
    }
}