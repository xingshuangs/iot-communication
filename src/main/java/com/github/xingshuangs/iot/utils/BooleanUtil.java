/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Boolean to byte tool.
 *
 * @author xingshuang
 */
public class BooleanUtil {

    private BooleanUtil() {
        // NOOP
    }

    /**
     * Boolean to byte. True is 0x01, false is 0x00.
     *
     * @param data boolean data
     * @return byte data.
     */
    public static byte toByte(boolean data) {
        return setBit((byte) 0x00, 0, data);
    }

    /**
     * Sets 1 or 0 for the specified bit of the byte.
     * (对字节的指定位设置1或0)
     *
     * @param bit bit index, [0,7]
     * @param res true：1，false：0
     * @return new byte
     */
    public static byte setBit(int bit, boolean res) {
        return setBit((byte) 0x00, bit, res);
    }

    /**
     * Sets 1 or 0 for the specified bit of the byte.
     * (对字节的指定位设置1或0)
     *
     * @param data source byte data
     * @param bit  bit index, [0,7]
     * @param res  true：1，false：0
     * @return new byte
     */
    public static byte setBit(byte data, int bit, boolean res) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("bit > 7 || bit < 0");
        }
        return res ? (byte) (((data & 0xFF) | (1 << bit)) & 0xFF) : (byte) ((data & 0xFF) & ~(1 << bit) & 0xFF);
    }

    /**
     * Gets the value of the byte specified bit. If bit is 1, return true, else false.
     * (获取字节指定位的值)
     *
     * @param data source byte data
     * @param bit  bit index, [0,7]
     * @return true or false
     */
    public static boolean getValue(byte data, int bit) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0 <= bit <= 7");
        }
        return (((data & 0xFF) & (1 << bit)) != 0);
    }

    /**
     * Gets the value of the byte specified bit. If bit is 1, then return true, else false.
     * (获取字节指定位的值)
     *
     * @param data source byte data
     * @param bit  bit index, [0,7]
     * @return true or false
     */
    public static int getValueToInt(byte data, int bit) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0 <= bit <= 7");
        }
        return (((data & 0xFF) & (1 << bit)) >> bit);
    }

    /**
     * Retrieves the specified number of boolean values from byte array. If the value of bit is 1, it is true, otherwise it is false.
     * (提取指定数量的boolean值)
     *
     * @param quantity boolean number
     * @param src      byte array
     * @return boolean list
     */
    public static List<Boolean> byteArrayToList(int quantity, byte[] src) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (src.length * 8 < quantity) {
            // quantity数量操作字节数组的位总和
            throw new IllegalArgumentException("The sum of the bits of the operation byte array");
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
     * Converts a boolean list to byte array.
     * (将boolean列表转换为字节数组)
     *
     * @param list boolean list
     * @return byte array.
     */
    public static byte[] listToByteArray(List<Boolean> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null or empty");
        }
        int index = 0;
        byte[] values = new byte[(list.size() / 8) + (list.size() % 8 == 0 ? 0 : 1)];
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
