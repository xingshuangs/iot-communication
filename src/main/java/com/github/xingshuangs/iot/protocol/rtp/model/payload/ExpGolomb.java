/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import lombok.Getter;

/**
 * 指数哥伦布编码
 *
 * @author xingshuang
 */
@Getter
public class ExpGolomb {
    /**
     * 数据源
     */
    private final ByteReadBuff readBuff;

    /**
     * 位索引
     */
    private int bitIndex;

    /**
     * 当前的字节数据
     */
    private byte currentByte;

    public ExpGolomb(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data");
        }
        this.readBuff = new ByteReadBuff(data);
        this.bitIndex = 0;
        this.currentByte = this.readBuff.getByte();
    }

    /**
     * 更新下一个字节，若没有则直接抛异常
     */
    private void updateNext() {
        this.bitIndex = 0;
        this.currentByte = this.readBuff.getByte();
    }

    /**
     * 读取1位
     *
     * @return 位结果
     */
    public int read1Bit() {
        if (this.bitIndex == 8) {
            this.updateNext();
        }
        int res = (this.currentByte >> (7 - this.bitIndex)) & 0x01;
        this.bitIndex++;
        return res;
    }

    /**
     * 读取指定位数
     *
     * @param size 位数量
     * @return 结果
     */
    public long readNBit(int size) {
        if (size > 32) {
            throw new IllegalArgumentException("size > 32");
        }

        long res = 0;
        for (int i = 0; i < size; i++) {
            res <<= 1;
            res |= this.read1Bit();
        }
        return res;
    }

    public void skipBit(int size) {
        this.readNBit(size);
    }

    /**
     * 前导零计数
     *
     * @return 零的个数
     */
    public int skipLZ() {
        int count = 0;
        while (this.read1Bit() == 0) {
            count++;
        }
        return count;
    }

    /**
     * 跳过一个无符号指数哥伦布编码数值
     */
    public void skipUE() {
        int count = this.skipLZ();
        this.readNBit(1 + count);
    }

    /**
     * 跳过一个有符号指数哥伦布编码数值
     */
    public void skipSE() {
        int count = this.skipLZ();
        this.readNBit(1 + count);
    }

    /**
     * 跳过多个缩放列表
     *
     * @param count 个数
     */
    public void skipScalingList(int count) {
        int lastScale = 8;
        int nextScale = 8;
        for (int i = 0; i < count; i++) {
            if (nextScale != 0) {
                int deltaScale = this.readSE();
                nextScale = (lastScale + deltaScale + 256) % 256;
            }
            lastScale = (nextScale == 0) ? lastScale : nextScale;
        }
    }

    /**
     * 读取一个boolean值
     *
     * @return true，false
     */
    public boolean readBoolean() {
        return this.read1Bit() == 1;
    }

    /**
     * 读取一个无符号指数哥伦布编码数值
     *
     * @return 无符号指数哥伦布编码数值
     */
    public int readUE() {
        int count = this.skipLZ();
        return (int) ((1 << count) - 1 + this.readNBit(count));
    }

    /**
     * 读取一个有符号指数哥伦布编码数值
     *
     * @return 有符号指数哥伦布编码数值
     */
    public int readSE() {
        int value = this.readUE();
        int sign = ((value & 0x01) << 1) - 1;
        return ((value >> 1) + (value & 0x01)) * sign;
    }


}
