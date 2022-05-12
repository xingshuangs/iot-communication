package com.github.xingshuangs.iot.utils;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
     * @param data   字节数组
     * @param offset 偏移量
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

    /**
     * 将字节转换为字符串
     *
     * @param data 字节数组
     * @return 字符串
     */
    public static String toStr(byte[] data) {
        return toStr(data, 0, data.length, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset) {
        return toStr(data, offset, data.length - offset, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 长度
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset, int length) {
        return toStr(data, offset, length, StandardCharsets.US_ASCII);
    }

    /**
     * 将字节转换为字符串
     *
     * @param data        字节数组
     * @param offset      偏移量
     * @param length      长度
     * @param charsetName 字符编码名称
     * @return 字符串
     */
    public static String toStr(byte[] data, int offset, int length, Charset charsetName) {
        if (offset < 0) {
            throw new IllegalArgumentException("偏移量offset < 0");
        }
        if (length < 0) {
            throw new IllegalArgumentException("长度length < 0");
        }
        if (offset > data.length || offset + length > data.length) {
            throw new IllegalArgumentException("偏移量 > 字节数组长度 || 偏移量 + 长度 > 字节数组长度");
        }
        return new String(data, offset, length, charsetName);
    }
}
