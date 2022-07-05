package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadInputRegisterRequestTest {
    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x04, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01};
        MbReadInputRegisterRequest mb = new MbReadInputRegisterRequest();
        mb.setFunctionCode(EMbFunctionCode.READ_INPUT_REGISTER);
        mb.setAddress(8);
        mb.setQuantity(1);
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x04, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01};
        MbReadInputRegisterRequest mb = MbReadInputRegisterRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_INPUT_REGISTER, mb.functionCode);
        assertEquals(8, mb.getAddress());
        assertEquals(1, mb.getQuantity());
    }
}