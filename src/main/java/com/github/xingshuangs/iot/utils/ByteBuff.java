package com.github.xingshuangs.iot.utils;


import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字节缓存
 *
 * @author xingshuang
 */
@Getter
public class ByteBuff {

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
    public ByteBuff(int capacity) {
        this.data = new byte[capacity];
    }

    public static ByteBuff newInstance(int capacity) {
        return new ByteBuff(capacity);
    }

    /**
     * 校验条件
     *
     * @param size 大小
     */
    private void checkCondition(int size) {
        if (this.offset + size > data.length) {
            throw new IllegalArgumentException("超过字节数组最大容量");
        }
    }

    /**
     * 添加字节数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putByte(byte src) {
        this.checkCondition(1);
        this.data[this.offset] = src;
        this.offset++;
        return this;
    }

    /**
     * 添加字节数组数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putBytes(byte[] src) {
        System.arraycopy(src, 0, this.data, this.offset, src.length);
        this.offset += src.length;
        return this;
    }

    /**
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putShort(short src) {
        return this.putShort(src, false);
    }

    /**
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putShort(int src) {
        this.checkCondition(2);
        return this.putBytes(ShortUtil.toByteArray(src, false));
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putInteger(int src) {
        return this.putInteger(src, false);
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putInteger(long src) {
        return this.putInteger(src, false);
    }

    /**
     * 添加long数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putLong(long src) {
        return this.putLong(src, false);
    }

    /**
     * 添加float数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putFloat(float src) {
        return this.putFloat(src, false);
    }

    /**
     * 添加double数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putDouble(double src) {
        return this.putDouble(src, false);
    }

    /**
     * 添加string数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteBuff putString(String src) {
        return this.putString(src, StandardCharsets.US_ASCII);
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putShort(short src, boolean littleEndian) {
        this.checkCondition(2);
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putShort(int src, boolean littleEndian) {
        this.checkCondition(2);
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putInteger(int src, boolean littleEndian) {
        this.checkCondition(4);
        return this.putBytes(IntegerUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putInteger(long src, boolean littleEndian) {
        this.checkCondition(4);
        return this.putBytes(IntegerUtil.toByteArray((int) src, littleEndian));
    }

    /**
     * 添加long数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putLong(long src, boolean littleEndian) {
        this.checkCondition(8);
        return this.putBytes(LongUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加float数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putFloat(float src, boolean littleEndian) {
        this.checkCondition(4);
        return this.putBytes(FloatUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加double数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteBuff putDouble(double src, boolean littleEndian) {
        this.checkCondition(8);
        return this.putBytes(FloatUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加string数据
     *
     * @param src      数据源
     * @param charsets 字符集类型
     * @return 对象本身
     */
    public ByteBuff putString(String src, Charset charsets) {
        this.checkCondition(src.length());
        return this.putBytes(src.getBytes(charsets));
    }
}
