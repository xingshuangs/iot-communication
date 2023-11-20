package com.github.xingshuangs.iot.protocol.melsec.service;

import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import org.junit.Test;

import static org.junit.Assert.*;


public class McAddressUtilTest {

    @Test
    public void parse() {
        McDeviceAddress d1000 = McAddressUtil.parse("D1000", 10);
        System.out.println(d1000);
    }
}