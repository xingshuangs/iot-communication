package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class BCDUtilTest {

    @Test
    public void toInt() {
    }

    @Test
    public void toByte() {
        byte[] expect = new byte[]{(byte) 0x23,(byte) 0x01};
        int i = BCDUtil.toInt(expect);
        System.out.println(i);
    }
}