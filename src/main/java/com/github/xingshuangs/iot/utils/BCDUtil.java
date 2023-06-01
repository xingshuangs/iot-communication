package com.github.xingshuangs.iot.utils;


/**
 * BCD码转换工具
 *
 * @author xingshuang
 */
public class BCDUtil {

    private BCDUtil() {
        // NOOP
    }

    public static int toInt(byte data) {
        return ((data >> 4) * 10) + (data & 0x0F);
    }

    public static int toInt(byte[] data) {
        int result = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            result += toInt(data[i]) * Math.pow(100, i);
        }
        return result;
    }

    public static byte toByte(int data) {
        if (data > 99 || data < 0) {
            throw new IllegalArgumentException("data > 99 || data < 0");
        }
        return (byte) (((data / 10) << 4) | (data % 10));
    }
}
