package com.github.oscura.iot.utils;



/**
 * @author xingshuang
 * @date 2021/2/17
 */
public class HexUtil {

    /**
     * 将字符串转换为16进制的数组
     *
     * @param src 字符串
     * @return 字节数组
     */
    public byte[] toHexArray(String src) {
        char[] chars = src.toCharArray();
        final byte[] out = new byte[chars.length >> 1];
        for (int i = 0; i < chars.length; i = i + 2) {
            int high = Character.digit(chars[i], 16) << 4;
            int low = Character.digit(chars[i + 1], 16);
            out[i / 2] = (byte) ((high | low) & 0xFF);
        }
        return out;
    }
}
