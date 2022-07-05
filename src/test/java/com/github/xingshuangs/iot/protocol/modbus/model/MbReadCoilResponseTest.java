package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadCoilResponseTest {

    @Test
    public void toByteArray() {
        MbReadCoilResponse mb = new MbReadCoilResponse();
        mb.setFunctionCode(EMbFunctionCode.READ_COIL);
        mb.setCount(3);
        mb.setCoilStatus(new byte[]{(byte) 0xCD, (byte) 0x6B, (byte) 0x05});
        assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0xCD, (byte) 0x6B, (byte) 0x05}, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x03, (byte) 0xCD, (byte) 0x6B, (byte) 0x05};
        MbReadCoilResponse mb = MbReadCoilResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_COIL, mb.functionCode);
        assertEquals(3, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0xCD, (byte) 0x6B, (byte) 0x05}, mb.getCoilStatus());
    }
}