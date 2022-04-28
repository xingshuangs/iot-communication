package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class ByteUtil {

    private ByteUtil() {
        // NOOP
    }

    /**
     * 将int转换为byte
     *
     * @param data int数据
     * @return byte数据
     */
    public static byte toByte(int data) {
        return (byte) (data & 0xFF);
    }

    /**
     * 将byte转换为 uint8
     *
     * @param data byte数据
     * @return uint8数据
     */
    public static int toUInt8(byte data) {
        return data & 0xFF;
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data 字节数组
     * @return int32数据
     */
    public static int toUInt8(byte[] data) {
        return toUInt8(data, 0);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return int32数据
     */
    public static int toUInt8(byte[] data, int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("data小于1个字节");
        }
        if (offset + 1 > data.length) {
            throw new IndexOutOfBoundsException("offset + 1 > 字节长度");
        }
        return (data[offset] & 0xFF);
    }
}
