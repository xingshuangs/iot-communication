package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class IntegerUtil {

    private IntegerUtil() {
        // NOOP
    }

    /**
     * 将short转换为字节数组
     *
     * @param data         short数据
     * @param littleEndian true:小端，false：大端
     * @return 字节数组
     */
    public static byte[] toByteArray(int data, boolean littleEndian) {
        byte[] bytes = new byte[4];

        if (littleEndian) {
            bytes[0] = (byte) ((data) & 0xFF);
            bytes[1] = (byte) ((data >> 8) & 0xFF);
            bytes[2] = (byte) ((data >> 16) & 0xFF);
            bytes[3] = (byte) ((data >> 24) & 0xFF);
        } else {
            bytes[0] = (byte) ((data >> 24) & 0xFF);
            bytes[1] = (byte) ((data >> 16) & 0xFF);
            bytes[2] = (byte) ((data >> 8) & 0xFF);
            bytes[3] = (byte) ((data) & 0xFF);
        }
        return bytes;
    }

    /**
     * 将short转换为字节数组，默认采用大端模式
     *
     * @param data short数据
     * @return 字节数组
     */
    public static byte[] toByteArray(int data) {
        return toByteArray(data, false);
    }

    /**
     * 将short转换为字节数组，默认采用大端模式
     *
     * @param data short数据
     * @return 字节数组
     */
    public static byte[] toByteArray(long data) {
        return toByteArray((int) data, false);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data 字节数组
     * @return int32数据
     */
    public static int toInt32(byte[] data) {
        return toInt32(data, 0, false);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @return int32数据
     */
    public static int toInt32(byte[] data, int offset) {
        return toInt32(data, offset, false);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return int32数据
     */
    public static int toInt32(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("data小于4个字节");
        }
        if (offset + 4 > data.length) {
            throw new IndexOutOfBoundsException("offset + 4 > 字节长度");
        }
        int b = littleEndian ? 3 : 0;
        int d = littleEndian ? 1 : -1;
        return (((data[offset + b - d * 0] & 0xFF) << 24)
                | ((data[offset + b - d * 1] & 0xFF) << 16)
                | ((data[offset + b - d * 2] & 0xFF) << 8)
                | ((data[offset + b - d * 3] & 0xFF) << 0));
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @return int32数据
     */
    public static int toInt32In3Bytes(byte[] data, int offset) {
        return toInt32In3Bytes(data, offset, false);
    }

    /**
     * 将字节数组转换为int32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return int32数据
     */
    public static int toInt32In3Bytes(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("data小于3个字节");
        }
        if (offset + 3 > data.length) {
            throw new IndexOutOfBoundsException("offset + 3 > 字节长度");
        }
        int b = littleEndian ? 2 : 0;
        int d = littleEndian ? 1 : -1;
        return (((data[offset + b - d * 0] & 0xFF) << 16)
                | ((data[offset + b - d * 1] & 0xFF) << 8)
                | ((data[offset + b - d * 2] & 0xFF) << 0));
    }

    /**
     * 将字节数组转换为uint32
     *
     * @param data 字节数组
     * @return uint32数据
     */
    public static long toUInt32(byte[] data) {
        return toUInt32(data, 0, false);
    }

    /**
     * 将字节数组转换为uint32
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @return uint32数据
     */
    public static long toUInt32(byte[] data, int offset) {
        return toUInt32(data, offset, false);
    }

    /**
     * 将字节数组转换为uint32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return uint32数据
     */
    public static long toUInt32(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("data小于4个字节");
        }
        if (offset + 4 > data.length) {
            throw new IndexOutOfBoundsException("offset + 4 > 字节长度");
        }
        int b = littleEndian ? 3 : 0;
        int d = littleEndian ? 1 : -1;
        return (((data[offset + b - d * 0] & 0xFF) << 24)
                | ((data[offset + b - d * 1] & 0xFF) << 16)
                | ((data[offset + b - d * 2] & 0xFF) << 8)
                | ((data[offset + b - d * 3] & 0xFF) << 0)) & 0xFFFFFFFFL;
    }
}
