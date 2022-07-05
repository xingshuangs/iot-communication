package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadDiscreteInputRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x02, (byte) 0x00, (byte) 0xC4, (byte) 0x00, (byte) 0x16};
        MbReadDiscreteInputRequest mb = new MbReadDiscreteInputRequest();
        mb.setFunctionCode(EMbFunctionCode.READ_DISCRETE_INPUT);
        mb.setAddress(196);
        mb.setQuantity(22);
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x02, (byte) 0x00, (byte) 0xC4, (byte) 0x00, (byte) 0x16};
        MbReadDiscreteInputRequest mb = MbReadDiscreteInputRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_DISCRETE_INPUT, mb.functionCode);
        assertEquals(196, mb.getAddress());
        assertEquals(22, mb.getQuantity());
    }
}