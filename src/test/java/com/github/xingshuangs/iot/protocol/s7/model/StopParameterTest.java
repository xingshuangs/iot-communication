package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class StopParameterTest {

    @Test
    public void byteArrayLength() {
        StopParameter parameter = new StopParameter();
        assertEquals(7, parameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        StopParameter parameter = new StopParameter();
        parameter.setPiService("P_PROGRAM");
        byte[] actual = parameter.toByteArray();
        byte[] expect = {(byte) 0x29,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void fromBytes() {
        byte[] data = {(byte) 0x29,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};

        StopParameter parameter = StopParameter.fromBytes(data);
        assertEquals(EFunctionCode.PLC_STOP, parameter.getFunctionCode());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00}, parameter.getUnknownBytes());
        assertEquals(9, parameter.getLengthPart());
        assertEquals("P_PROGRAM", parameter.getPiService());
    }
}