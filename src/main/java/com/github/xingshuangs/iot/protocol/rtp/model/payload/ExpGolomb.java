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
 * Exp golomb encode class.
 * (指数哥伦布编码)
 *
 * @author xingshuang
 */
@Getter
public class ExpGolomb {

    /**
     * Data source.
     * (数据源)
     */
    private final ByteReadBuff readBuff;

    /**
     * Bit index.
     * (位索引)
     */
    private int bitIndex;

    /**
     * Current byte.
     * (当前的字节数据)
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
     * Update next byte.
     * (更新下一个字节，若没有则直接抛异常)
     */
    private void updateNext() {
        this.bitIndex = 0;
        this.currentByte = this.readBuff.getByte();
    }

    /**
     * Read 1 bit.
     * (读取1位)
     *
     * @return bit result
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
     * Read N bit.
     * (读取指定位数)
     *
     * @param size bit size
     * @return result
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

    /**
     * Skip size bit.
     * (跳过指定size的bit数量)
     *
     * @param size bit数量
     */
    public void skipBit(int size) {
        this.readNBit(size);
    }

    /**
     * Skip leading zero.
     * (前导零计数)
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
     * Skip an unsigned exponential golomb coded value.
     * (跳过一个无符号指数哥伦布编码数值)
     */
    public void skipUE() {
        this.readUE();
    }

    /**
     * Skip a signed exponential golomb coded value
     * (跳过一个有符号指数哥伦布编码数值)
     */
    public void skipSE() {
        this.readSE();
    }

    /**
     * Skip multiple zoom lists.
     * (跳过多个缩放列表)
     *
     * @param count count
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
     * Read a boolean.
     * (读取一个boolean值)
     *
     * @return true，false
     */
    public boolean readBoolean() {
        return this.read1Bit() == 1;
    }

    /**
     * Reads an unsigned exponential golomb coded value.
     * (读取一个无符号指数哥伦布编码数值)
     *
     * @return an unsigned value
     */
    public int readUE() {
        int count = this.skipLZ();
        return (int) ((1 << count) - 1 + this.readNBit(count));
    }

    /**
     * Read a signed exponential golomb coded valuel
     * (读取一个有符号指数哥伦布编码数值)
     *
     * @return a signed value
     */
    public int readSE() {
        int value = this.readUE();
        int sign = ((value & 0x01) << 1) - 1;
        return ((value >> 1) + (value & 0x01)) * sign;
    }
}
