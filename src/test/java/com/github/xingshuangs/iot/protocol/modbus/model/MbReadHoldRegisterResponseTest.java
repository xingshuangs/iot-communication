package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadHoldRegisterResponseTest {

    @Test
    public void toByteArray() {
        MbReadHoldRegisterResponse mb = new MbReadHoldRegisterResponse();
        mb.setFunctionCode(EMbFunctionCode.READ_HOLD_REGISTER);
        mb.setCount(6);
        mb.setRegister(new byte[]{(byte) 0x02, (byte) 0x2B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64});
        assertArrayEquals(new byte[]{(byte) 0x03, (byte) 0x06, (byte) 0x02, (byte) 0x2B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64}, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x03, (byte) 0x06, (byte) 0x02, (byte) 0x2B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64};
        MbReadHoldRegisterResponse mb = MbReadHoldRegisterResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_HOLD_REGISTER, mb.functionCode);
        assertEquals(6, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0x02, (byte) 0x2B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64}, mb.getRegister());
    }
}