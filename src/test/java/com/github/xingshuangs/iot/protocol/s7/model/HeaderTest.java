package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import org.junit.Test;

import static org.junit.Assert.*;


public class HeaderTest {

    @Test
    public void byteArrayLength() {
        Header header = new Header();
        assertEquals(12, header.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        Header header = new Header();
        header.setProtocolId((byte) 0x32);
        header.setMessageType(EMessageType.JOB);
        header.setReserved(0x0000);
        header.setPduReference(0x0000);
        header.setParameterLength(0x0000);
        header.setDataLength(0x0002);
        header.setErrorClass(EErrorClass.NO_ERROR);
        header.setErrorCode((byte) 0x00);
        byte[] actual = header.toByteArray();
        byte[] expect = new byte[]{(byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expect, actual);
    }
}