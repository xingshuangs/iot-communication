package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class ShortUtil {

    /**
     * 将short转换为字节数组
     *
     * @param data         short数据
     * @param littleEndian true:小端，false：大端
     * @return 字节数组
     */
    public static byte[] toByteArray(short data, boolean littleEndian) {
        byte[] bytes = new byte[2];

        if (littleEndian) {
            bytes[0] = (byte) (data & 0xFF);
            bytes[1] = (byte) (data >> 8 & 0xFF);
        } else {
            bytes[0] = (byte) (data >> 8 & 0xFF);
            bytes[1] = (byte) (data & 0xFF);
        }
        return bytes;
    }

    /**
     * 将short转换为字节数组，默认采用大端模式
     *
     * @param data short数据
     * @return 字节数组
     */
    public static byte[] toByteArray(short data) {
        return toByteArray(data, false);
    }
}
