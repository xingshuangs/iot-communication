package com.github.xingshuangs.iot.protocol.s7.service;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class S7PLC1Test {
    private S7PLC1 s7PLC1 = new S7PLC1();

    @Test
    public void readUInt16() throws IOException {
        int i = s7PLC1.readUInt16("");
        assertEquals(1280, i);
    }
}