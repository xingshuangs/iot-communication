package com.github.oscura.iot.utils;

import com.github.oscura.iot.exceptions.HexParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexUtilTest {

    @Test
    public void toHexArray_One() {
        byte[] bytes = HexUtil.toHexArray("1A");
        assertEquals(0x1A, bytes[0]);
    }

    @Test
    public void toHexArray_Multi() {
        byte[] actual = HexUtil.toHexArray("1a6BdE8c");
        byte[] expected = new byte[]{(byte) 0x1A, (byte) 0x6B, (byte) 0xDE, (byte) 0x8C};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_NotEven() {
         HexUtil.toHexArray("1a6BdE8");
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_Null() {
        HexUtil.toHexArray(null);
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_Empty() {
        HexUtil.toHexArray("");
    }

    @Test(expected = HexParseException.class)
    public void toHexArray_NotHex() {
        HexUtil.toHexArray("ii");
    }
}
