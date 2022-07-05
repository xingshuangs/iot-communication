package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadInputRegisterResponseTest {
    @Test
    public void toByteArray() {
        MbReadInputRegisterResponse mb = new MbReadInputRegisterResponse();
        mb.setFunctionCode(EMbFunctionCode.READ_INPUT_REGISTER);
        mb.setCount(2);
        mb.setRegister(new byte[]{(byte) 0x00, (byte) 0x0A});
        assertArrayEquals(new byte[]{(byte) 0x04, (byte) 0x02, (byte) 0x00, (byte) 0x0A}, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x04, (byte) 0x02, (byte) 0x00, (byte) 0x0A};
        MbReadInputRegisterResponse mb = MbReadInputRegisterResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_INPUT_REGISTER, mb.functionCode);
        assertEquals(2, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x0A}, mb.getRegister());
    }
}