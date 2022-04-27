package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class COTPTest {

    @Test
    public void byteArrayLength() {
        COTP cotp = new COTP();
        assertEquals(2, cotp.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        COTP cotp = new COTP();
        cotp.setLength((byte) 0x03);
        cotp.setPduType(EPduType.CONNECT_CONFIRM);
        byte[] target = new byte[]{(byte) 0x03, (byte) 0xD0};
        assertArrayEquals(target, cotp.toByteArray());
    }
}