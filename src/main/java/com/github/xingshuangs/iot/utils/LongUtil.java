package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class LongUtil {

    private LongUtil() {

    }

    /**
     * 将short转换为字节数组
     *
     * @param data short数据
     * @return 字节数组
     */
    public static byte[] toByteArray(long data) {
        return toByteArray(data, false);
    }

    /**
     * 将short转换为字节数组
     *
     * @param data         short数据
     * @param littleEndian true:小端，false：大端
     * @return 字节数组
     */
    public static byte[] toByteArray(long data, boolean littleEndian) {
        byte[] bytes = new byte[8];

        if (littleEndian) {
            bytes[0] = (byte) ((data) & 0xFF);
            bytes[1] = (byte) ((data >> 8) & 0xFF);
            bytes[2] = (byte) ((data >> 16) & 0xFF);
            bytes[3] = (byte) ((data >> 24) & 0xFF);
            bytes[4] = (byte) ((data >> 32) & 0xFF);
            bytes[5] = (byte) ((data >> 40) & 0xFF);
            bytes[6] = (byte) ((data >> 48) & 0xFF);
            bytes[7] = (byte) ((data >> 56) & 0xFF);
        } else {
            bytes[0] = (byte) ((data >> 56) & 0xFF);
            bytes[1] = (byte) ((data >> 48) & 0xFF);
            bytes[2] = (byte) ((data >> 40) & 0xFF);
            bytes[3] = (byte) ((data >> 32) & 0xFF);
            bytes[4] = (byte) ((data >> 24) & 0xFF);
            bytes[5] = (byte) ((data >> 16) & 0xFF);
            bytes[6] = (byte) ((data >> 8) & 0xFF);
            bytes[7] = (byte) ((data) & 0xFF);
        }
        return bytes;
    }
}
