package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbWriteMultipleRegisterResponseTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x10, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02};
        MbWriteMultipleRegisterResponse mb = new MbWriteMultipleRegisterResponse();
        mb.setFunctionCode(EMbFunctionCode.WRITE_MULTIPLE_REGISTER);
        mb.setAddress(1);
        mb.setQuantity(2);
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x10, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02};
        MbWriteMultipleRegisterResponse mb = MbWriteMultipleRegisterResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.WRITE_MULTIPLE_REGISTER, mb.functionCode);
        assertEquals(1, mb.getAddress());
        assertEquals(2, mb.getQuantity());
    }
}