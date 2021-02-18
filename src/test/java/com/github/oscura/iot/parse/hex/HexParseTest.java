package com.github.oscura.iot.parse.hex;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HexParseTest {

    private HexParse hexParse;

    @Before
    public void init() {
        this.hexParse = new HexParse(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x81, (byte) 0x00,
                (byte) 0x00, (byte) 0x64, (byte) 0x59, (byte) 0xFF, (byte) 0xFF, (byte) 0x9B, (byte) 0xA7});
    }

    @Test
    public void toBoolean() {
        List<Boolean> list = this.hexParse.toBoolean(3, 0, 1);
        assertArrayEquals(new Boolean[]{true}, list.toArray(new Boolean[0]));
        list = this.hexParse.toBoolean(3, 0, 3);
        assertArrayEquals(new Boolean[]{true, false, false}, list.toArray(new Boolean[0]));
        list = this.hexParse.toBoolean(3, 6, 4);
        assertArrayEquals(new Boolean[]{false, true, false, false}, list.toArray(new Boolean[0]));
    }

    @Test
    public void toInt8() {
        List<Byte> list = this.hexParse.toInt8(1, 2);
        assertArrayEquals(new Byte[]{(byte) 0xFF, (byte) 0xFF}, list.toArray(new Byte[0]));
        list = this.hexParse.toInt8(2, 3);
        assertArrayEquals(new Byte[]{(byte) 0xFF, (byte) 0x81, (byte) 0x00}, list.toArray(new Byte[0]));
    }

    @Test
    public void toUInt8() {
        List<Integer> list = this.hexParse.toUInt8(1, 2);
        assertArrayEquals(new Integer[]{0xFF, 0xFF}, list.toArray(new Integer[0]));
        list = this.hexParse.toUInt8(2, 3);
        assertArrayEquals(new Integer[]{0xFF, 0x81, 0x00}, list.toArray(new Integer[0]));
    }

    @Test
    public void toInt16() {
        List<Short> list = this.hexParse.toInt16(0, 2, false);
        assertArrayEquals(new Short[]{-1, (short)-127}, list.toArray(new Short[0]));
        list = this.hexParse.toInt16(4, 2, false);
        assertArrayEquals(new Short[]{0, (short)25689}, list.toArray(new Short[0]));
    }

    @Test
    public void toUInt16() {
        List<Integer> list = this.hexParse.toUInt16(0, 2, false);
        assertArrayEquals(new Integer[]{65535, 65409}, list.toArray(new Integer[0]));
        list = this.hexParse.toUInt16(4, 2, false);
        assertArrayEquals(new Integer[]{0, 25689}, list.toArray(new Integer[0]));
    }

    @Test
    public void toInt32() {
        List<Integer> list = this.hexParse.toInt32(0, 1, false);
        assertArrayEquals(new Integer[]{-127}, list.toArray(new Integer[0]));
    }

    @Test
    public void toUInt32() {
        List<Long> list = this.hexParse.toUInt32(0, 1, false);
        assertArrayEquals(new Long[]{4294967169L}, list.toArray(new Long[0]));
    }
}
