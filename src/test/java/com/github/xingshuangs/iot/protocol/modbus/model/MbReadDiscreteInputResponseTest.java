package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadDiscreteInputResponseTest {

    @Test
    public void toByteArray() {
        MbReadDiscreteInputResponse mb = new MbReadDiscreteInputResponse();
        mb.setFunctionCode(EMbFunctionCode.READ_DISCRETE_INPUT);
        mb.setCount(3);
        mb.setInputStatus(new byte[]{(byte) 0xAC, (byte) 0xDB, (byte) 0x35});
        assertArrayEquals(new byte[]{(byte) 0x02, (byte) 0x03, (byte) 0xAC, (byte) 0xDB, (byte) 0x35}, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x02, (byte) 0x03, (byte) 0xAC, (byte) 0xDB, (byte) 0x35};
        MbReadDiscreteInputResponse mb = MbReadDiscreteInputResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_DISCRETE_INPUT, mb.functionCode);
        assertEquals(3, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0xAC, (byte) 0xDB, (byte) 0x35}, mb.getInputStatus());
    }
}