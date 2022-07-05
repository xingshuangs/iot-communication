package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbWriteMultipleRegisterRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x10, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x04, (byte) 0x00, (byte) 0x0A, (byte) 0x01, (byte) 0x02};
        MbWriteMultipleRegisterRequest mb = new MbWriteMultipleRegisterRequest();
        mb.setFunctionCode(EMbFunctionCode.WRITE_MULTIPLE_REGISTER);
        mb.setAddress(1);
        mb.setQuantity(2);
        mb.setCount(4);
        mb.setValue(new byte[]{(byte) 0x00, (byte) 0x0A, (byte) 0x01, (byte) 0x02});
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x10, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x04, (byte) 0x00, (byte) 0x0A, (byte) 0x01, (byte) 0x02};
        MbWriteMultipleRegisterRequest mb = MbWriteMultipleRegisterRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.WRITE_MULTIPLE_REGISTER, mb.functionCode);
        assertEquals(1, mb.getAddress());
        assertEquals(2, mb.getQuantity());
        assertEquals(4, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x0A, (byte) 0x01, (byte) 0x02}, mb.getValue());
    }
}