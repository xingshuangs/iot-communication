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

    public List<Byte> toInt8(int byteOffset, int count) {
        return this.toHandle(byteOffset, count, 1, i -> this.rdSrc[byteOffset + i]);
    }

    public List<Integer> toUInt8(int byteOffset, int count) {
        return this.toHandle(byteOffset, count, 1, i -> this.rdSrc[byteOffset + i] & 0xFF);
    }

    public Short toInt16(int byteOffset) {
        return this.toInt16(byteOffset, 1, false).get(0);
    }

    public List<Short> toInt16(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 2;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            if (littleEndian) {
                return (short) (this.rdSrc[byteOffset + i * typeByteLength + 1] << 8 | this.rdSrc[byteOffset + i * typeByteLength] & 0xFF);
            } else {
                return (short) (this.rdSrc[byteOffset + i * typeByteLength] << 8 | this.rdSrc[byteOffset + i * typeByteLength + 1] & 0xFF);
            }
        });
    }

    public List<Integer> toUInt16(int byteOffset, int count, boolean littleEndian) {
        int typeByteLength = 2;
        return this.toHandle(byteOffset, count, typeByteLength, i -> {
            if (littleEndian) {
                return (this.rdSrc[byteOffset + i * typeByteLength + 1] << 8 | this.rdSrc[byteOffset + i * typeByteLength] & 0xFF) & 0xFFFF;
            } else {
                return (this.rdSrc[byteOffset + i * typeByteLength] << 8 | this.rdSrc[byteOffset + i * typeByteLength + 1] & 0xFF) & 0xFFFF;
            }
        });
    }
}
