/*
  Copyright (C), 2009-2021, 江苏汇博机器人技术股份有限公司
  FileName: HexParse
  Author:   ShuangPC
  Date:     2021/2/18
  Description: 16进制数据解析
  History:
  <author>         <time>          <version>          <desc>
  作者姓名         修改时间           版本号             描述
 */

package com.github.oscura.iot.parse.hex;

import com.github.oscura.iot.exceptions.HexParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 16进制数据解析
 *
 * @author ShuangPC
 * @date 2021/2/18
 */

public class HexParse {

    /**
     * 数据源
     */
    private byte[] rdSrc;

    public HexParse() {
        this(new byte[0]);
    }

    public HexParse(byte[] rdSrc) {
        this.rdSrc = rdSrc;
    }

    private <T> List<T> toHandle(int byteOffset, int count, double typeByteLength, Function<Integer, T> fun) {
        if (byteOffset < 0 || byteOffset >= this.rdSrc.length) {
            throw new HexParseException(String.format("字节偏移量[%d] 超过 总数据长度[%d]", byteOffset, this.rdSrc.length));
        }
        if (count < 1) {
            throw new HexParseException(String.format("获取的数据个数[%d] < 1", count));
        }
        if (byteOffset + count * typeByteLength > this.rdSrc.length) {
            throw new HexParseException(String.format("字节偏移量[%d] + 数据个数[%d] * 类型字节长度[%f] > 总数据字节长度[%d]", byteOffset, count, 0.125, this.rdSrc.length));
        }
        List<T> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            res.add(fun.apply(i));
        }
        return res;
    }

    public Boolean toBoolean(int byteOffset, int bitOffset) {
        return this.toBoolean(byteOffset, bitOffset, 1).get(0);
    }

    public List<Boolean> toBoolean(int byteOffset, int bitOffset, int count) {
        if (bitOffset < 0 || bitOffset > 7) {
            throw new HexParseException("bitOffset位偏移量范围[0,7]");
        }

        return this.toHandle(byteOffset, count, 0.125, i -> {
            int bitAdd = (bitOffset + i) % 8;
            int byteAdd = byteOffset + (bitOffset + i) / 8;
            return ((this.rdSrc[byteAdd] >> bitAdd) & 0x01) == 0x01;
        });
    }

    public Byte toInt8(int byteOffset) {
        return this.toInt8(byteOffset, 1).get(0);
    }

    /**
     * 获取Int8数据列表
     *
     * @param byteOffset 字节偏移量
     * @param count      个数
     * @return Int8数据列表
     */
    public List<Byte> toInt8(int byteOffset, int count) {
        return this.toHandle(byteOffset, count, 1, i -> this.rdSrc[byteOffset + i]);
    }

    public Integer toUInt8(int byteOffset) {
        return this.toUInt8(byteOffset, 1).get(0);
    }

    /**
     * 获取UInt8数据列表
     *
     * @param byteOffset 字节偏移量
     * @param count      个数
     * @return UInt8数据列表
     */
    public List<Integer> toUInt8(int byteOffset, int count) {
        return this.toHandle(byteOffset, count, 1, i -> this.rdSrc[byteOffset + i] & 0xFF);
    }

    /**
     * 获取Int16的数据
     *
     * @param byteOffset 字节偏移量
     * @return Int16数据
     */
    public Short toInt16(int byteOffset) {
        return this.toInt16(byteOffset, 1, false).get(0);
    }

    /**
     * 获取Int16的数据列表
     *
     * @param byteOffset   字节偏移量
     * @param count        个数
     * @param littleEndian 小端模式
     * @return Int16的数据列表
     */
    public List<Short> toInt16(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 2;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            int b = littleEndian ? 0 : 1;
            return (short) (this.rdSrc[byteOffset + i * typeByteLength + 1 - b] << 8 | this.rdSrc[byteOffset + i * typeByteLength + b] & 0xFF);
        });
    }

    /**
     * 获取单个UInt16的数据
     *
     * @param byteOffset 字节偏移量
     * @return UInt16数据
     */
    public Integer toUInt16(int byteOffset) {
        return this.toUInt16(byteOffset, 1, false).get(0);
    }

    /**
     * 获取UInt16的数据列表
     *
     * @param byteOffset   字节偏移量
     * @param count        个数
     * @param littleEndian 小端模式
     * @return UInt16的数据列表
     */
    public List<Integer> toUInt16(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 2;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            int b = littleEndian ? 0 : 1;
            return (this.rdSrc[byteOffset + i * typeByteLength + 1 - b] << 8 | this.rdSrc[byteOffset + i * typeByteLength + b] & 0xFF) & 0xFFFF;
        });
    }

    public Integer toInt32(int byteOffset) {
        return this.toInt32(byteOffset, 1, false).get(0);
    }

    /**
     * 获取Int32的数据列表
     *
     * @param byteOffset   字节偏移量
     * @param count        个数
     * @param littleEndian 小端模式
     * @return Int32的数据列表
     */
    public List<Integer> toInt32(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 4;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            int b = littleEndian ? 3 : 0;
            int d = littleEndian ? 1 : -1;
            return (this.rdSrc[byteOffset + i * typeByteLength + b - d * 0] << 24 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 1] << 16 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 2] << 8 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 3]);
        });
    }

    public Long toUInt32(int byteOffset) {
        return this.toUInt32(byteOffset, 1, false).get(0);
    }

    /**
     * 获取UInt32的数据列表
     *
     * @param byteOffset   字节偏移量
     * @param count        个数
     * @param littleEndian 小端模式
     * @return UInt32的数据列表
     */
    public List<Long> toUInt32(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 4;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            int b = littleEndian ? 3 : 0;
            int d = littleEndian ? 1 : -1;
            return (this.rdSrc[byteOffset + i * typeByteLength + b - d * 0] << 24 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 1] << 16 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 2] << 8 |
                    this.rdSrc[byteOffset + i * typeByteLength + b - d * 3]) & 0xFFFFFFFFL;
        });
    }
}
