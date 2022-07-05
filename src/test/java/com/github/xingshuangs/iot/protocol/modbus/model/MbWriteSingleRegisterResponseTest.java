package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbWriteSingleRegisterResponseTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x03};
        MbWriteSingleRegisterResponse mb = new MbWriteSingleRegisterResponse();
        mb.setFunctionCode(EMbFunctionCode.WRITE_SINGLE_REGISTER);
        mb.setAddress(1);
        mb.setValue(new byte[]{(byte) 0x00, (byte) 0x03});
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x03};
        MbWriteSingleRegisterResponse mb = MbWriteSingleRegisterResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.WRITE_SINGLE_REGISTER, mb.functionCode);
        assertEquals(1, mb.getAddress());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x03}, mb.getValue());
    }
}