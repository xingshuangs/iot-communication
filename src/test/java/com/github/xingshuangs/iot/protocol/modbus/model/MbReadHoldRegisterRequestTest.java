package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadHoldRegisterRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x6B, (byte) 0x00, (byte) 0x03};
        MbReadHoldRegisterRequest mb = new MbReadHoldRegisterRequest();
        mb.setFunctionCode(EMbFunctionCode.READ_HOLD_REGISTER);
        mb.setAddress(107);
        mb.setQuantity(3);
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x6B, (byte) 0x00, (byte) 0x03};
        MbReadHoldRegisterRequest mb = MbReadHoldRegisterRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_HOLD_REGISTER, mb.functionCode);
        assertEquals(107, mb.getAddress());
        assertEquals(3, mb.getQuantity());
    }
}