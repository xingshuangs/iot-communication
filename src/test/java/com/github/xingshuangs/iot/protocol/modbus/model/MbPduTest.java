package com.github.xingshuangs.iot.protocol.modbus.model;

import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class MbPduTest {

    @Test
    public void toByteArray() {

        Assert.assertThrows(ModbusCommException.class, () -> {
            byte[] bytes = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, (byte) 0x80, (byte) 0xFF};
            MbTcpResponse mbTcpResponse = MbTcpResponse.fromBytes(bytes);
        });
    }
}