package com.github.xingshuangs.iot.utils;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 字节读取缓存
 *
 * @author xingshuang
 */
public class ByteReadBuff {

    /**
     * 数据
     */
    private final byte[] data;

    /**
     * 偏移量
     */
    private int offset = 0;

    /**
     * 构造方法
     *
     * @param capacity 容量
     */
    public ByteReadBuff(byte[] data) {
        this.data = data;
    }

    public static ByteReadBuff newInstance(byte[] data) {
        return new ByteReadBuff(data);
    }

    /**
     * 校验条件
     *
     * @param index 索引
     */
    private void checkCondition(int index) {
        if (index >= data.length) {
            throw new IndexOutOfBoundsException("超过字节数组最大容量");
        }
    }

    /**
     * 获取boolean类型数据
     *
     * @param bit 位地址
     * @return boolean数据
     */
    public boolean getBoolean(int bit) {
        boolean res = this.getBoolean(this.offset, bit);
        this.offset++;
        return res;
    }

    /**
     * 获取1个字节数据
     *
     * @return 字节数据
     */
    public byte getByte() {
        byte res = this.getByte(this.offset);
        this.offset++;
        return res;
    }

    /**
     * 获取字节数组数据
     *
     * @param length 长度
     * @return 字节数组
     */
    public byte[] getBytes(int length) {
        byte[] res = this.getBytes(this.offset, length);
        this.offset += length;
        return res;
    }

    /**
     * 获取1个字节的整形数据
     *
     * @return int数据
     */
    public int getByteToInt() {
        int res = this.getByteToInt(this.offset);
        this.offset++;
        return res;
    }

    /**
     * 获取int16数据
     *
     * @return int16数据
     */
    public short getInt16() {
        short res = this.getInt16(this.offset);
        this.offset += 2;
        return res;
    }

    /**
     * 获取uint16数据
     *
     * @return uint16数据
     */
    public int getUInt16() {
        int res = this.getUInt16(this.offset);
        this.offset += 2;
        return res;
    }

    /**
     * 获取int32数据
     *
     * @return int32数据
     */
    public int getInt32() {
        int res = this.getInt32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * 获取uint32数据
     *
     * @return uint32数据
     */
    public long getUInt32() {
        long res = this.getUInt32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * 获取float32数据
     *
     * @return float32数据
     */
    public float getFloat32() {
        float res = this.getFloat32(this.offset);
        this.offset += 4;
        return res;
    }

    /**
     * 获取float64数据
     *
     * @return float64数据
     */
    public double getFloat64() {
        double res = this.getFloat64(this.offset);
        this.offset += 8;
        return res;
    }

    /**
     * 获取字符串数据
     *
     * @param length 字符串长度
     * @return 字符串数据
     */
    public String getString(int length) {
        String res = this.getString(this.offset, length);
        this.offset += length;
        return res;
    }

    /**
     * 获取字符串数据
     *
     * @param length  字符串长度
     * @param charset 字符集
     * @return 字符串数据
     */
    public String getString(int length, Charset charset) {
        String res = this.getString(this.offset, length, charset);
        this.offset += length;
        return res;
    }

    /**
     * 获取boolean数据
     *
     * @param index 索引
     * @param bit   位
     * @return boolean数据
     */
    public boolean getBoolean(int index, int bit) {
        this.checkCondition(index);
        return BooleanUtil.getValue(this.data[index], bit);
    }

    /**
     * 获取字节数据
     *
     * @param index 索引
     * @return 字节
     */
    public byte getByte(int index) {
        this.checkCondition(index);
        return this.data[index];
    }

    /**
     * 获取字节数组数据
     *
     * @param index  索引
     * @param length 长度
     * @return 字节数组
     */
    public byte[] getBytes(int index, int length) {
        this.checkCondition(index + length - 1);
        return Arrays.copyOfRange(this.data, index, index + length);
    }

    /**
     * 获取一个字节的整形数据
     *
     * @param index 索引
     * @return int数据
     */
    public int getByteToInt(int index) {
        this.checkCondition(index);
        return ByteUtil.toUInt8(this.data, index);
    }

    /**
     * 获取int16数据
     *
     * @param index 索引
     * @return int16数据
     */
    public short getInt16(int index) {
        this.checkCondition(index);
        return ShortUtil.toInt16(this.data, index);
    }

    /**
     * 获取uint16数据
     *
     * @param index 索引
     * @return uint16数据
     */
    public int getUInt16(int index) {
        this.checkCondition(index);
        return ShortUtil.toUInt16(this.data, index);
    }

    /**
     * 获取int32数据
     *
     * @param index 索引
     * @return int32数据
     */
    public int getInt32(int index) {
        this.checkCondition(index);
        return IntegerUtil.toInt32(this.data, index);
    }

    /**
     * 获取uint32数据
     *
     * @param index 索引
     * @return uint32数据
     */
    public long getUInt32(int index) {
        this.checkCondition(index);
        return IntegerUtil.toUInt32(this.data, index);
    }

    /**
     * 获取float32数据
     *
     * @param index 索引
     * @return float32数据
     */
    public float getFloat32(int index) {
        this.checkCondition(index);
        return FloatUtil.toFloat32(this.data, index);
    }

    /**
     * 获取float64数据
     *
     * @param index 索引
     * @return float64数据
     */
    public double getFloat64(int index) {
        this.checkCondition(index);
        return FloatUtil.toFloat64(this.data, index);
    }

    /**
     * 获取字符串
     *
     * @param index  索引
     * @param length 长度
     * @return 字符串
     */
    public String getString(int index, int length) {
        return this.getString(index, length, StandardCharsets.US_ASCII);
    }

    /**
     * 获取字符串
     *
     * @param index   索引
     * @param length  长度
     * @param charset 字符集
     * @return 字符串
     */
    public String getString(int index, int length, Charset charset) {
        this.checkCondition(index + length - 1);
        return ByteUtil.toStr(this.data, index, length, charset);
    }
}
