package com.github.xingshuangs.iot.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author xingshuang
 */
public class BooleanUtil {

    private BooleanUtil() {
        // NOOP
    }

    public static byte toByte(boolean data) {
        return setBit((byte) 0x00, 0, data);
    }

    /**
     * 对字节的指定位设置1或0
     *
     * @param bit 位数
     * @param res true：1，false：0
     * @return 新的字节
     */
    public static byte setBit(int bit, boolean res) {
        return setBit((byte) 0x00, bit, res);
    }

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
        return res ? (byte) (((data & 0xFF) | (1 << bit)) & 0xFF) : (byte) ((data & 0xFF) & ~(1 << bit) & 0xFF);
    }

    /**
     * 获取字节指定位的状态
     *
     * @param data 字节数据
     * @param bit  位数 0-7
     * @return 结果状态，true，false
     */
    public static boolean getValue(byte data, int bit) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0<=bit<=7");
        }
        return (((data & 0xFF) & (1 << bit)) != 0);
    }

    /**
     * 获取字节指定位的状态
     *
     * @param data 字节数据
     * @param bit  位数 0-7
     * @return 结果状态，true，false
     */
    public static int getValueToInt(byte data, int bit) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0<=bit<=7");
        }
        return (((data & 0xFF) & (1 << bit)) >> bit);
    }

    /**
     * 提取指定数量的boolean值
     *
     * @param quantity 数量
     * @param src      数据源
     * @return boolean列表
     */
    public static List<Boolean> byteArrayToList(int quantity, byte[] src) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (src.length * 8 < quantity) {
            throw new IllegalArgumentException("quantity数量操作字节数组的位总和");
        }
        int count = 1;
        List<Boolean> res = new ArrayList<>();
        for (byte data : src) {
            for (int j = 0; j < 8; j++) {
                if (count <= quantity) {
                    res.add(BooleanUtil.getValue(data, j));
                    count++;
                }
            }
        }
        return res;
    }

    /**
     * 将boolean列表转换为字节数组
     *
     * @param list boolean列表
     * @return 字节数组
     */
    public static byte[] listToByteArray(List<Boolean> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list为空");
        }
        int index = 0;
        byte[] values = new byte[list.size() / 8 + list.size() % 8 == 0 ? 0 : 1];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < list.size()) {
                    values[i] = BooleanUtil.setBit(values[i], j, list.get(index));
                    index++;
                }
            }
        }
        return values;
    }
}
