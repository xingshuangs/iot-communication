package com.github.xingshuangs.iot.protocol.s7.service;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.Assert.*;


public class S7PLCTest {
    private S7PLC s7PLC = new S7PLC();

    @Test
    public void readUInt16() throws IOException {
        int i = s7PLC.readUInt16("");
        System.out.println(i);
    }


    @Test
    public void readUInt16Multi() throws IOException {
        List<Integer> list = s7PLC.readUInt32("");
        list.forEach(System.out::println);
    }

    @Test
    public void readBoolean() throws IOException{
        boolean actual = s7PLC.readBoolean("DB1.3.0");
        assertTrue(actual);
        actual = s7PLC.readBoolean("DB1.3.1");
        assertTrue(actual);
        actual = s7PLC.readBoolean("DB1.3.2");
        assertTrue(actual);
        actual = s7PLC.readBoolean("DB1.3.3");
        assertFalse(actual);
        actual = s7PLC.readBoolean("DB1.3.4");
        assertFalse(actual);
    }
}