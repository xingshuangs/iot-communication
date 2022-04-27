package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class SetupComParameterTest {

    @Test
    public void byteArrayLength() {
        SetupComParameter setupComParameter = new SetupComParameter();
        assertEquals(8, setupComParameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        SetupComParameter setupComParameter = new SetupComParameter();
        setupComParameter.setFunctionCode(EFunctionCode.READ_VARIABLE);
        setupComParameter.setReserved((byte)0x00);
        setupComParameter.setMaxAmqCaller(0x0001);
        setupComParameter.setMaxAmqCallee(0x0001);
        setupComParameter.setPduLength(0x0004);
        byte[] actual = setupComParameter.toByteArray();
        byte[] expect = {(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x04};
        assertArrayEquals(expect, actual);
    }
}