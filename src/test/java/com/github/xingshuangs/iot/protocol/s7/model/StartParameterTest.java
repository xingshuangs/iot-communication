package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class StartParameterTest {

    @Test
    public void byteArrayLength() {
        StartParameter parameter = new StartParameter();
        assertEquals(11, parameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        StartParameter parameter = new StartParameter();
        parameter.setParameterBlock("");
        parameter.setPiService(StartParameter.P_PROGRAM);
        byte[] actual = parameter.toByteArray();
        byte[] expect = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void fromBytes() {
        byte[] data = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x00,
                (byte) 0x09,
                (byte) 0x50, (byte) 0x5f, (byte) 0x50, (byte) 0x52, (byte) 0x4F, (byte) 0x47, (byte) 0x52, (byte) 0x41, (byte) 0x4D};

        StartParameter parameter = StartParameter.fromBytes(data);
        assertEquals(EFunctionCode.PLC_CONTROL, parameter.getFunctionCode());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD}, parameter.getUnknownBytes());
        assertEquals(0, parameter.getParameterBlockLength());
        assertEquals("", parameter.getParameterBlock());
        assertEquals(9, parameter.getLengthPart());
        assertEquals(StartParameter.P_PROGRAM, parameter.getPiService());
    }


}