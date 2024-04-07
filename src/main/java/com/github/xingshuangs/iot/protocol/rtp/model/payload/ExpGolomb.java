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

/**
 * 指数哥伦布编码
 *
 * @author xingshuang
 */
public class ExpGolomb {

    private int bitNumber;

    private ByteReadBuff readBuff;

    private byte currentByte;

    public ExpGolomb(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data");
        }
        this.readBuff = new ByteReadBuff(data);
        this.bitNumber = 0;
        this.currentByte = this.readBuff.getByte();
    }

    private void updateNext() {
        this.bitNumber = 0;
        this.currentByte = this.readBuff.getByte();
    }

    public int read1Bit() {
        if (this.bitNumber == 8) {
            this.updateNext();
        }
        int res = (this.currentByte >> (7 - this.bitNumber)) & 0x01;
        this.bitNumber++;
        return res;
    }

    public int skipLZ(){
        int count = 0;
        while (this.read1Bit() == 0) {
            count++;
        }
        return count;
    }

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

    public int readUE() {
        int count = 0;
        while (this.read1Bit() == 0) {
            count++;
        }

        return (int) ((1 << count) - 1 + this.readNBit(count));
    }

    public boolean readBoolean() {
        return this.read1Bit() == 1;
    }

    public int readSE() {
        int value = this.readUE();
        int sign = ((value & 0x01) << 1) - 1;
        return ((value >> 1) + (value & 0x01)) * sign;
    }
}
