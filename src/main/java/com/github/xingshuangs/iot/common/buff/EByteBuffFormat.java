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

package com.github.xingshuangs.iot.common.buff;


import com.github.xingshuangs.iot.exceptions.HexParseException;

/**
 * 4 - or 8-byte encoding format.
 * (字节缓存格式)
 *
 * @author xingshuang
 */
public enum EByteBuffFormat {
    /**
     * In the original order.
     * (按照原始顺序排列)
     */
    AB_CD("AB_CD"),

    /**
     * Invert as a single byte.
     * (按照单字节反转)
     */
    BA_DC("BA_DC"),

    /**
     * Reverse by double byte.
     * (按照双字节反转)
     */
    CD_AB("CD_AB"),

    /**
     * In reverse order.
     * (按照倒序排列)
     */
    DC_BA("DC_BA");

    private final String code;

    EByteBuffFormat(String code) {
        this.code = code;
    }

    /**
     * The 4-byte data is reformatted in EByteBuffFormat.
     * (4字节数据按EByteBuffFormat重新格式化)
     *
     * @param data byte array
     * @return new 4-byte array.
     */
    public byte[] formatIn4Bytes(byte[] data) {
        return this.formatIn4Bytes(data, 0);
    }

    /**
     * The 4-byte data is reformatted in EByteBuffFormat.
     * (4字节数据按EByteBuffFormat重新格式化)
     *
     * @param data  byte array
     * @param index index
     * @return new 4-byte array.
     */
    public byte[] formatIn4Bytes(byte[] data, int index) {
        byte[] res = new byte[4];
        switch (this.code) {
            case "AB_CD":
                res[0] = data[index + 3];
                res[1] = data[index + 2];
                res[2] = data[index + 1];
                res[3] = data[index + 0];
                break;
            case "BA_DC":
                res[0] = data[index + 2];
                res[1] = data[index + 3];
                res[2] = data[index + 0];
                res[3] = data[index + 1];
                break;
            case "CD_AB":
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                break;
            case "DC_BA":
                res[0] = data[index + 0];
                res[1] = data[index + 1];
                res[2] = data[index + 2];
                res[3] = data[index + 3];
                break;
            default:
                // 未实现该数据格式
                throw new HexParseException("The data format is not implemented");
        }
        return res;
    }

    /**
     * The 8-byte data is reformatted in EByteBuffFormat.
     * (8字节数据按EByteBuffFormat重新格式化)
     *
     * @param data 数据源
     * @return new 8-byte array.
     */
    public byte[] formatIn8Bytes(byte[] data) {
        return this.formatIn8Bytes(data, 0);
    }

    /**
     * The 4-byte data is reformatted in EByteBuffFormat.
     * (8字节数据按EByteBuffFormat重新格式化)
     *
     * @param data  byte array
     * @param index index
     * @return new 8-byte array.
     */
    public byte[] formatIn8Bytes(byte[] data, int index) {
        byte[] res = new byte[8];
        switch (this.code) {
            case "AB_CD":
                res[0] = data[index + 7];
                res[1] = data[index + 6];
                res[2] = data[index + 5];
                res[3] = data[index + 4];
                res[4] = data[index + 3];
                res[5] = data[index + 2];
                res[6] = data[index + 1];
                res[7] = data[index + 0];
                break;
            case "BA_DC":
                res[0] = data[index + 6];
                res[1] = data[index + 7];
                res[2] = data[index + 4];
                res[3] = data[index + 5];
                res[4] = data[index + 2];
                res[5] = data[index + 3];
                res[6] = data[index + 0];
                res[7] = data[index + 1];
                break;
            case "CD_AB":
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                res[4] = data[index + 5];
                res[5] = data[index + 4];
                res[6] = data[index + 7];
                res[7] = data[index + 6];
                break;
            case "DC_BA":
                res[0] = data[index + 0];
                res[1] = data[index + 1];
                res[2] = data[index + 2];
                res[3] = data[index + 3];
                res[4] = data[index + 4];
                res[5] = data[index + 5];
                res[6] = data[index + 6];
                res[7] = data[index + 7];
                break;
            default:
                throw new HexParseException("The data format is not implemented");
        }
        return res;
    }
}
