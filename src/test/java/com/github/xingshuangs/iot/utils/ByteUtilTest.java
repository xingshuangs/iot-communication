package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class ByteUtilTest {

    @Test
    public void toUInt8() {
        System.out.println((byte)0x8F);
        System.out.println((byte)0x8F & 0xFF);
    }
}