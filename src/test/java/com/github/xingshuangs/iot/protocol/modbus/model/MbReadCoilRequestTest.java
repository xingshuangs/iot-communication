package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbReadCoilRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x13};
        MbReadCoilRequest mb = new MbReadCoilRequest();
        mb.setFunctionCode(EMbFunctionCode.READ_COIL);
        mb.setAddress(19);
        mb.setQuantity(19);
        assertEquals(5, mb.byteArrayLength());
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x13};
        MbReadCoilRequest mb = MbReadCoilRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.READ_COIL, mb.functionCode);
        assertEquals(19, mb.getAddress());
        assertEquals(19, mb.getQuantity());
    }
}