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
import lombok.Data;

/**
 * 字节缓存基类
 *
 * @author xingshuang
 */
@Data
public class ByteBuffBase {

    private EByteBuffFormat byteBuffFormat;

    public ByteBuffBase() {
        this(EByteBuffFormat.DC_BA);
    }

    public ByteBuffBase(EByteBuffFormat byteBuffFormat) {
        this.byteBuffFormat = byteBuffFormat;
    }

    /**
     * 4字节数据根据格式重新排序
     *
     * @param data 字节数组数据
     * @return 4字节数组重排结果
     */
    protected byte[] reorderByFormatIn4Bytes(byte[] data) {
        return this.reorderByFormatIn4Bytes(data, 0);
    }

    /**
     * 4字节数据根据格式重新排序
     *
     * @param data  字节数组数据
     * @param index 索引
     * @return 4字节数组重排结果
     */
    protected byte[] reorderByFormatIn4Bytes(byte[] data, int index) {
        byte[] res = new byte[4];
        switch (this.byteBuffFormat) {
            case AB_CD:
                res[0] = data[index + 3];
                res[1] = data[index + 2];
                res[2] = data[index + 1];
                res[3] = data[index + 0];
                break;
            case BA_DC:
                res[0] = data[index + 2];
                res[1] = data[index + 3];
                res[2] = data[index + 0];
                res[3] = data[index + 1];
                break;
            case CD_AB:
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                break;
            case DC_BA:
                res[0] = data[index + 0];
                res[1] = data[index + 1];
                res[2] = data[index + 2];
                res[3] = data[index + 3];
                break;
            default:
                throw new HexParseException("未实现该数据格式");
        }
        return res;
    }

    /**
     * 8字节数据根据格式重新排序
     *
     * @param data 字节数组数据
     * @return 8字节数组重排结果
     */
    protected byte[] reorderByFormatIn8Bytes(byte[] data) {
        return this.reorderByFormatIn8Bytes(data, 0);
    }

    /**
     * 8字节数据根据格式重新排序
     *
     * @param data  字节数组数据
     * @param index 索引
     * @return 8字节数组重排结果
     */
    protected byte[] reorderByFormatIn8Bytes(byte[] data, int index) {
        byte[] res = new byte[8];
        switch (this.byteBuffFormat) {
            case AB_CD:
                res[0] = data[index + 7];
                res[1] = data[index + 6];
                res[2] = data[index + 5];
                res[3] = data[index + 4];
                res[4] = data[index + 3];
                res[5] = data[index + 2];
                res[6] = data[index + 1];
                res[7] = data[index + 0];
                break;
            case BA_DC:
                res[0] = data[index + 6];
                res[1] = data[index + 7];
                res[2] = data[index + 4];
                res[3] = data[index + 5];
                res[4] = data[index + 2];
                res[5] = data[index + 3];
                res[6] = data[index + 0];
                res[7] = data[index + 1];
                break;
            case CD_AB:
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                res[4] = data[index + 5];
                res[5] = data[index + 4];
                res[6] = data[index + 7];
                res[7] = data[index + 6];
                break;
            case DC_BA:
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
                throw new HexParseException("未实现该数据格式");
        }
        return res;
    }
}
