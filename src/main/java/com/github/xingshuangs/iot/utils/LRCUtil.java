package com.github.xingshuangs.iot.utils;


/**
 * LRC校验
 *
 * @author xingshuang
 */
public class LRCUtil {

    private LRCUtil() {
        // NOOP
    }

    /**
     * lrc校验值
     *
     * @param src 字节数组
     * @return byte校验值
     */
    public static byte lrc(byte[] src) {
        if (src == null || src.length == 0) {
            throw new IllegalArgumentException("src");
        }

        int sum = 0;
        for (byte b : src) {
            sum += b;
        }
        sum = sum % 256;
        sum = 256 - sum;
        return (byte) sum;
    }

    /**
     * lrc校验
     *
     * @param src    字节数组
     * @param target 目标比较值
     * @return true：一致，false：不一致
     */
    public static boolean lrc(byte[] src, byte target) {
        byte des = lrc(src);
        return des == target;
    }
}
