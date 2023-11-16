package com.github.xingshuangs.iot.protocol.melsec.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class McMessageAckTest {

    @Test
    public void mcDeviceBatchReadAckData() {
        byte[] src = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src);
        McHeaderAck header = ack.getHeader();
        assertEquals(0x0054, header.getSubHeader());
        Mc4E3EFrameAccessRoute accessRoute = (Mc4E3EFrameAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(12, header.getDataLength());
        McAckData data = (McAckData) ack.getData();
        assertEquals(10, data.getData().length);
        byte[] dataBytes = new byte[]{
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        assertArrayEquals(dataBytes, data.getData());
    }
}