package com.github.xingshuangs.iot.protocol.melsec.model;

import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class McDeviceAddressTest {

    @Test(expected = McCommException.class)
    public void createByNoLetter() {
        McDeviceAddress.createBy("133");
    }

    @Test(expected = McCommException.class)
    public void createByNoNumber() {
        McDeviceAddress.createBy("D");
    }

    @Test
    public void createBy1() {
        McDeviceAddress d133 = McDeviceAddress.createBy("D133");
        assertEquals(EMcDeviceCode.D, d133.getDeviceCode());
        assertEquals(133, d133.getHeadDeviceNumber());
        assertEquals(1, d133.getDevicePointsCount());
    }

    @Test
    public void createBy2() {
        byte[] data = new byte[]{0x01, 0x02};
        McDeviceContent d133 = McDeviceContent.createBy("D133", data);
        assertEquals(EMcDeviceCode.D, d133.getDeviceCode());
        assertEquals(133, d133.getHeadDeviceNumber());
        assertEquals(1, d133.getDevicePointsCount());
        assertArrayEquals(data, d133.getData());
    }
}