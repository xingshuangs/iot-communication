package com.github.xingshuangs.iot.utils;


import com.github.xingshuangs.iot.exceptions.HexParseException;

/**
 * @author xingshuang
 * @date 2021/2/17
 */
public class HexUtil {

    /**
     * 验证16进制字符串的正则表达式
     * ^ = 开始
     * $ = 结束
     * + = 匹配前面的子表达式一次或多次。
     * [] = 表达式的开始和结束
     */
    private static final String REGEX = "^[a-f0-9A-F]+$";

    /**
     * 将字符串转换为16进制的数组
     *
     * @param src 字符串
     * @return 字节数组
     */
    public static byte[] toHexArray(String src) {
        if (src == null || src.length() == 0) {
            throw new HexParseException("字符串不能为null或长度不能为0");
        }
        if ((src.length() & -src.length()) == 1) {
            throw new HexParseException("输入的字符串个数必须为偶数");
        }
        if (!src.matches(REGEX)) {
            throw new HexParseException("字符串内容必须是[0-9|a-f|A-F]");
        }

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
