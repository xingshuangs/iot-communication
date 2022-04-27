package com.github.xingshuangs.iot.utils;


/**
 * @author xingshuang
 */
public class FloatUtil {

    private FloatUtil() {
        // NOOP
    }

    /**
     * 将字节数组转换为float32
     *
     * @param data 字节数组
     * @return float32数据
     */
    public static float toFloat32(byte[] data) {
        return toFloat32(data, 0, false);
    }

    /**
     * 将字节数组转换为float32
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @return float32数据
     */
    public static float toFloat32(byte[] data, int offset) {
        return toFloat32(data, offset, false);
    }

    /**
     * 将字节数组转换为float32
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return float32数据
     */
    public static float toFloat32(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("data小于4个字节");
        }
        if (offset + 4 > data.length) {
            throw new IndexOutOfBoundsException("offset + 4 > 字节长度");
        }
        int b = littleEndian ? 3 : 0;
        int d = littleEndian ? 1 : -1;
        int l= (data[offset + b - d * 0] << 24 |
                data[offset + b - d * 1] << 16 |
                data[offset + b - d * 2] << 8 |
                data[offset + b - d * 3]);
        return Float.intBitsToFloat(l);
    }

    /**
     * 将字节数组转换为float64
     *
     * @param data 字节数组
     * @return float64数据
     */
    public static double toFloat64(byte[] data) {
        return toFloat64(data, 0, false);
    }

    /**
     * 将字节数组转换为float64
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @return float64数据
     */
    public static double toFloat64(byte[] data, int offset) {
        return toFloat64(data, offset, false);
    }

    /**
     * 将字节数组转换为float64
     *
     * @param data         字节数组
     * @param offset       偏移量
     * @param littleEndian true：小端模式，false：大端模式
     * @return float64数据
     */
    public static double toFloat64(byte[] data, int offset, boolean littleEndian) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("data小于8个字节");
        }
        if (offset + 8 > data.length) {
            throw new IndexOutOfBoundsException("offset + 8 > 字节长度");
        }
        int b = littleEndian ? 7 : 0;
        int d = littleEndian ? 1 : -1;
        long l = ((long) data[offset +  b - d * 0] << 56)
                | (((long) data[offset +  b - d * 1] << 48) & 0xffffffffffffffL)
                | (((long) data[offset +  b - d * 2] << 40) & 0xffffffffffffL)
                | (((long) data[offset +  b - d * 3] << 32) & 0xffffffffffL)
                | (((long) data[offset +  b - d * 4] << 24) & 0xffffffffL)
                | (((long) data[offset +  b - d * 5] << 16) & 0xffffffL)
                | (((long) data[offset +  b - d * 6] << 8) & 0xffffL)
                | ((long) data[offset +  b - d * 7] & 0xffL);
        return Double.longBitsToDouble(l);
    }
}
