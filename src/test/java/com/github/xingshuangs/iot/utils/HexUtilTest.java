package com.github.xingshuangs.iot.utils;

import com.github.xingshuangs.iot.exceptions.HexParseException;
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

    @Test
    public void toHexString() {
        String expected = "1A 6B DE 8C";
        byte[] data = new byte[]{(byte) 0x1A, (byte) 0x6B, (byte) 0xDE, (byte) 0x8C};
        String actual = HexUtil.toHexString(data);
        assertEquals(expected, actual);

        expected = "A1 49 AB DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data);
        assertEquals(expected, actual);

        expected = "A1|49|AB|DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"|");
        assertEquals(expected, actual);

        expected = "A1-49-AB-DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"-");
        assertEquals(expected, actual);

        expected = "A1@49@AB@DF";
        data = new byte[]{(byte) 0xA1, (byte) 0x49, (byte) 0xAB, (byte) 0xDF};
        actual = HexUtil.toHexString(data,"@");
        assertEquals(expected, actual);
    }
}
