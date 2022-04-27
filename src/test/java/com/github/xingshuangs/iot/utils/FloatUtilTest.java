package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class FloatUtilTest {

    @Test
    public void toFloat32() {
        byte[] data = new byte[]{(byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7};
        float actual = FloatUtil.toFloat32(data);
        assertEquals(33.16f, actual, 0.00001);
        data = new byte[]{(byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85};
        actual = FloatUtil.toFloat32(data);
        assertEquals(-15.62f, actual, 0.001);
    }

    @Test
    public void toFloat64() {
        byte[] data = new byte[]{(byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7};
        double actual = FloatUtil.toFloat64(data);
        assertEquals(156665.35455556, actual, 0.000000001);
        data = new byte[]{(byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7};
        actual = FloatUtil.toFloat64(data);
        assertEquals(-56516.66664, actual, 0.000001);
    }
}