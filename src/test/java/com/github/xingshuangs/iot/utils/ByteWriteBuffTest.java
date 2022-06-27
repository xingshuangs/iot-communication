package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import static org.junit.Assert.*;


public class ByteWriteBuffTest {

    @Test
    public void putByte() {
        ByteWriteBuff buff = new ByteWriteBuff(1);
        buff.putByte((byte) 0x55);
        assertArrayEquals(new byte[]{(byte) 0x55}, buff.getData());
        assertEquals(1, buff.getOffset());
    }

    @Test
    public void putBytes() {
        ByteWriteBuff buff = new ByteWriteBuff(3);
        buff.putBytes(new byte[]{(byte) 0x55, (byte) 0x56, (byte) 0x57});
        assertArrayEquals(new byte[]{(byte) 0x55, (byte) 0x56, (byte) 0x57}, buff.getData());
        assertEquals(3, buff.getOffset());
    }

    @Test
    public void putShort() {
        ByteWriteBuff buff = new ByteWriteBuff(2);
        buff.putShort((short) 24565);
        assertArrayEquals(new byte[]{(byte) 0x5F, (byte) 0xF5}, buff.getData());
        assertEquals(2, buff.getOffset());
    }

    @Test
    public void putShort1() {
        ByteWriteBuff buff = new ByteWriteBuff(2);
        buff.putShort(24565);
        assertArrayEquals(new byte[]{(byte) 0x5F, (byte) 0xF5}, buff.getData());
        assertEquals(2, buff.getOffset());
    }

    @Test
    public void putInteger() {
        ByteWriteBuff buff = new ByteWriteBuff(4);
        buff.putInteger(2111286);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36}, buff.getData());
        assertEquals(4, buff.getOffset());
    }

    @Test
    public void putInteger1() {
        ByteWriteBuff buff = new ByteWriteBuff(4);
        buff.putInteger(2111286L);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36}, buff.getData());
        assertEquals(4, buff.getOffset());
    }

    @Test
    public void putLong() {
        ByteWriteBuff buff = new ByteWriteBuff(8);
        buff.putLong(2111286L);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36}, buff.getData());
        assertEquals(8, buff.getOffset());
    }

    @Test
    public void putFloat() {
        ByteWriteBuff buff = new ByteWriteBuff(4);
        buff.putFloat(33.16f);
        assertArrayEquals(new byte[]{(byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7}, buff.getData());
        assertEquals(4, buff.getOffset());
    }

    @Test
    public void putDouble() {
        ByteWriteBuff buff = new ByteWriteBuff(8);
        buff.putDouble(156665.35455556);
        assertArrayEquals(new byte[]{(byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7}, buff.getData());
        assertEquals(8, buff.getOffset());
    }

    @Test
    public void putString() {
        ByteWriteBuff buff = new ByteWriteBuff(4);
        buff.putString("0123");
        assertArrayEquals(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33}, buff.getData());
        assertEquals(4, buff.getOffset());
    }
}