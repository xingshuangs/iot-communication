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

package com.github.xingshuangs.iot.net;


/**
 * Communicate interface.
 * (通信接口)
 *
 * @author xingshuang
 */
public interface ICommunicable {

    /**
     * Close the communication.
     * (关闭)
     */
    void close();

    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data byte array
     */
    void write(final byte[] data);

    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data   byte array
     * @param offset the start offset in the data.
     * @param length the number of bytes to write.
     */
    void write(final byte[] data, final int offset, final int length);

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data byte array
     * @return the total number of bytes read into the data
     */
    int read(final byte[] data);

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data   byte array
     * @param offset the start offset in the data.
     * @param length the number of bytes to read.
     * @return the total number of bytes read into the data
     */
    int read(final byte[] data, final int offset, final int length);
}
