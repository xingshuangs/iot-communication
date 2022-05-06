package com.github.xingshuangs.iot.protocol.s7.service;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class S7PLCTest {
    private S7PLC s7PLC = new S7PLC();

    @Test
    public void readUInt16() throws IOException {
        int i = s7PLC.readUInt16("");
        assertEquals(1280, i);
    }
}