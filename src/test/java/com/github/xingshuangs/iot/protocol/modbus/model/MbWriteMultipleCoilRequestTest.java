package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbWriteMultipleCoilRequestTest {

    @Test
    public void toByteArray() {
        byte[] actual = new byte[]{(byte) 0x0F, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0A, (byte) 0x02, (byte) 0xCD, (byte) 0x01};
        MbWriteMultipleCoilRequest mb = new MbWriteMultipleCoilRequest();
        mb.setFunctionCode(EMbFunctionCode.WRITE_MULTIPLE_COIL);
        mb.setAddress(19);
        mb.setQuantity(10);
        mb.setCount(2);
        mb.setValue(new byte[]{(byte) 0xCD, (byte) 0x01});
        assertArrayEquals(actual, mb.toByteArray());
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{(byte) 0x0F, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0A, (byte) 0x02, (byte) 0xCD, (byte) 0x01};
        MbWriteMultipleCoilRequest mb = MbWriteMultipleCoilRequest.fromBytes(data);
        assertEquals(EMbFunctionCode.WRITE_MULTIPLE_COIL, mb.functionCode);
        assertEquals(19, mb.getAddress());
        assertEquals(10, mb.getQuantity());
        assertEquals(2, mb.getCount());
        assertArrayEquals(new byte[]{(byte) 0xCD, (byte) 0x01}, mb.getValue());
    }

}