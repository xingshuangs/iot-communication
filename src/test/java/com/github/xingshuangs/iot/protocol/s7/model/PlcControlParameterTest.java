package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlcControlParameterTest {

    @Test
    public void byteArrayLength() {
        PlcControlParameter parameter = new PlcControlParameter();
        assertEquals(11, parameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock(new PlcControlStringParamBlock());
        parameter.setPiService(PlcControlParameter.P_PROGRAM);
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

        PlcControlParameter parameter = PlcControlParameter.fromBytes(data);
        assertEquals(EFunctionCode.PLC_CONTROL, parameter.getFunctionCode());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD}, parameter.getUnknownBytes());
        assertEquals(0, parameter.getParameterBlockLength());
        assertEquals(0, parameter.getParameterBlock().byteArrayLength());
        assertEquals(9, parameter.getLengthPart());
        assertEquals(PlcControlParameter.P_PROGRAM, parameter.getPiService());
    }

    @Test
    public void insert() {
        byte[] expect = {(byte) 0x28,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD,
                (byte) 0x00, (byte) 0x0A,
                (byte) 0x01, (byte) 0x00,
                (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x50,
                (byte) 0x05, (byte) 0x5F, (byte) 0x49, (byte) 0x4e, (byte) 0x53, (byte) 0x45};

        PlcControlParameter insert = PlcControlParameter.insert(EFileBlockType.DB, 1, EDestinationFileSystem.P);
        assertArrayEquals(expect, insert.toByteArray());

    }
}