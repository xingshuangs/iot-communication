package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class BooleanUtil {

    /**
     * 对字节的指定位设置1或0
     *
     * @param data 字节数据
     * @param bit  位数
     * @param res  true：1，false：0
     * @return 新的字节
     */
    public static byte setBit(byte data, int bit, boolean res) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0<=bit<=7");
        }
        return res ? (byte) (((data & 0xFF) | (1 << bit)) & 0xFF) : (byte) (data & ~(1 << bit) & 0xFF);
    }
}
