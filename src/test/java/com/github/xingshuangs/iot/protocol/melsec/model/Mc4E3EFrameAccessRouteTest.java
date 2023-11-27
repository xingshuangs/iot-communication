package com.github.xingshuangs.iot.protocol.melsec.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class Mc4E3EFrameAccessRouteTest {

    @Test
    public void fromBytes() {
        byte[] src = new byte[]{0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,};
        McFrame4E3EAccessRoute accessRoute = McFrame4E3EAccessRoute.fromBytes(src);
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
    }
}