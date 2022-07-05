package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbWriteSingleCoilResponseTest {
    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x05, (byte) 0x00, (byte) 0xAC, (byte) 0xFF, (byte) 0x00};
        MbWriteSingleCoilResponse mb = new MbWriteSingleCoilResponse();
        mb.setFunctionCode(EMbFunctionCode.WRITE_SINGLE_COIL);
        mb.setAddress(172);
        mb.setValue(new byte[]{(byte) 0xFF, (byte) 0x00});
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x05, (byte) 0x00, (byte) 0xAC, (byte) 0xFF, (byte) 0x00};
        MbWriteSingleCoilResponse mb = MbWriteSingleCoilResponse.fromBytes(data);
        assertEquals(EMbFunctionCode.WRITE_SINGLE_COIL, mb.functionCode);
        assertEquals(172, mb.getAddress());
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0x00}, mb.getValue());
    }
}