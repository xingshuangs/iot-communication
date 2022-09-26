package com.github.xingshuangs.iot.protocol.common.buff;


import com.github.xingshuangs.iot.utils.*;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 写字节缓存
 *
 * @author xingshuang
 */
@Getter
public class ByteWriteBuff extends ByteBuffBase {

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
    public ByteWriteBuff(int capacity) {
        this.data = new byte[capacity];
    }

    public static ByteWriteBuff newInstance(int capacity) {
        return new ByteWriteBuff(capacity);
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
    public ByteWriteBuff putByte(byte src) {
        this.checkCondition(1);
        this.data[this.offset] = src;
        this.offset++;
        return this;
    }

    /**
     * 添加int类型单字节数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putByte(int src) {
        this.checkCondition(1);
        return this.putByte(ByteUtil.toByte(src));
    }

    /**
     * 添加字节数组数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putBytes(byte[] src) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        this.checkCondition(src.length);
        System.arraycopy(src, 0, this.data, this.offset, src.length);
        this.offset += src.length;
        return this;
    }

    /**
     * 添加字节数组数据
     *
     * @param src        数据源
     * @param startIndex 起始索引
     * @return 对象本身
     */
    public ByteWriteBuff putBytes(byte[] src, int startIndex) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        this.checkCondition(src.length - startIndex);
        System.arraycopy(src, startIndex, this.data, this.offset, src.length - startIndex);
        this.offset += src.length;
        return this;
    }

    /**
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putShort(short src) {
        return this.putShort(src, false);
    }

    /**
     * 添加short数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putShort(int src) {
        return this.putBytes(ShortUtil.toByteArray(src, false));
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(int src) {
        return this.putInteger(src, false);
    }

    /**
     * 添加integer数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src) {
        return this.putInteger(src, false);
    }

    /**
     * 添加long数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src) {
        return this.putLong(src, false);
    }

    /**
     * 添加float数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src) {
        return this.putFloat(src, false);
    }

    /**
     * 添加double数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src) {
        return this.putDouble(src, false);
    }

    /**
     * 添加string数据
     *
     * @param src 数据源
     * @return 对象本身
     */
    public ByteWriteBuff putString(String src) {
        return this.putString(src, StandardCharsets.US_ASCII);
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putShort(short src, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加short数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putShort(int src, boolean littleEndian) {
        return this.putBytes(ShortUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(int src, boolean littleEndian) {
        return this.putBytes(IntegerUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加integer数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putInteger(long src, boolean littleEndian) {
        return this.putBytes(IntegerUtil.toByteArray((int) src, littleEndian));
    }

    /**
     * 添加long数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putLong(long src, boolean littleEndian) {
        return this.putBytes(LongUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加float数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putFloat(float src, boolean littleEndian) {
        return this.putBytes(FloatUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加double数据
     *
     * @param src          数据源
     * @param littleEndian 是否小端模式
     * @return 对象本身
     */
    public ByteWriteBuff putDouble(double src, boolean littleEndian) {
        return this.putBytes(FloatUtil.toByteArray(src, littleEndian));
    }

    /**
     * 添加string数据
     *
     * @param src      数据源
     * @param charsets 字符集类型
     * @return 对象本身
     */
    public ByteWriteBuff putString(String src, Charset charsets) {
        return this.putBytes(src.getBytes(charsets));
    }
}
