package com.github.xingshuangs.iot.protocol.modbus.model;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class MbRtuCommTest {

    @Test
    public void mbRtuRequestTest() {
        byte[] expect = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 0xC5, (byte) 0xCD};
        MbReadHoldRegisterRequest pdu = new MbReadHoldRegisterRequest(0, 10);
        MbRtuRequest request = new MbRtuRequest(1, pdu);
        byte[] actual = request.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void mbRtuResponseTest() {
        byte[] expect = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0x14, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1E, (byte) 0x1C};
        MbRtuResponse response = MbRtuResponse.fromBytes(expect);
        assertEquals(1, response.getUnitId());
        assertArrayEquals(new byte[]{(byte) 0x1E, (byte) 0x1C}, response.getCrc());
        MbReadHoldRegisterResponse holdRegister = (MbReadHoldRegisterResponse) response.getPdu();
        assertEquals(20, holdRegister.getCount());
        byte[] expectData = new byte[]{(byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expectData, holdRegister.getRegister());
    }
}