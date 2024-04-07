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


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket通信工具
 *
 * @author xingshuang
 */
public class SocketUtils {

    private SocketUtils() {
        // NOOP
    }

    /**
     * 关闭socket
     *
     * @param socket socket对象
     * @throws IOException IO异常
     */
    public static void close(final Socket socket) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * 是否连接状态
     *
     * @param socket socket对象
     * @return true：连接，false：断开
     */
    public static boolean isConnected(final Socket socket) {
        return socket != null && (socket.isConnected() && !socket.isClosed());
    }

    /**
     * 读取数据
     *
     * @param socket socket对象
     * @param data   字节数组
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data) throws IOException {
        return read(socket, data, 0, data.length, -1, 0);
    }

    /**
     * 读取数据
     *
     * @param socket socket对象
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 读取长度
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data, int offset, final int length) throws IOException {
        return read(socket, data, offset, length, -1, 0);
    }

    /**
     * 读取数据
     *
     * @param socket    socket对象
     * @param data      字节数组
     * @param offset    偏移量
     * @param length    读取长度
     * @param maxLength 单次通信允许的对最大长度
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data, int offset, final int length, final int maxLength) throws IOException {
        return read(socket, data, offset, length, maxLength, 0);
    }

    /**
     * 读取数据
     *
     * @param socket    socket对象
     * @param data      字节数组
     * @param offset    偏移量
     * @param length    读取长度
     * @param maxLength 单次通信允许的对最大长度
     * @param timeout   超时时间，0：没有超时时间
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data, final int offset, final int length,
                           final int maxLength, final int timeout) throws IOException {
        return read(socket, data, offset, length, maxLength, timeout, false);
    }

    /**
     * 读取数据
     *
     * @param socket      socket对象
     * @param data        字节数组
     * @param offset      偏移量
     * @param length      读取长度
     * @param maxLength   单次通信允许的对最大长度
     * @param timeout     超时时间，0：没有超时时间，无限等
     * @param waitForMore 若数据不够，是否等待，等待更多数据，大部分都是不等待的，等待都适用于分包粘包的情况
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data, final int offset, final int length,
                           final int maxLength, final int timeout, final boolean waitForMore) throws IOException {
        if (offset + length > data.length) {
            throw new IllegalArgumentException("offset+length");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout>=0");
        }
        // 阻塞不是指read的时间长短，可以理解为没有数据可读，线程一直在这等待
        socket.setSoTimeout(timeout);
        // 读取个数
        int count = 0;
        // 读取位置平移量，会变
        int off = offset;
        InputStream in = socket.getInputStream();
        while (count < length) {
            int len = maxLength <= 0 ? length - count : Math.min(maxLength, length - count);
            int num = in.read(data, off, len);
            if (num < 0) {
                // 读取数据异常，未读取到数据，连接断开
                throw new SocketRuntimeException("The data is not read, and the connection is disconnected");
            }
            count += num;
            off += num;
            // 实际读取的数据长度比期望还小，则证明已经读取完了
            if (!waitForMore && num < len) {
                break;
            }
        }
        return count;
    }

    /**
     * 写入数据
     *
     * @param socket socket对象
     * @param data   字节数组
     * @throws IOException IO异常
     */
    public static void write(final Socket socket, final byte[] data) throws IOException {
        write(socket, data, 0, data.length, -1);
    }

    /**
     * 写入数据
     *
     * @param socket socket对象
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 写入长度
     * @throws IOException IO异常
     */
    public static void write(final Socket socket, final byte[] data, int offset, final int length) throws IOException {
        write(socket, data, offset, length, -1);
    }

    /**
     * 写入数据
     *
     * @param socket    socket对象
     * @param data      字节数组
     * @param offset    偏移量
     * @param length    写入长度
     * @param maxLength 单次通信允许的对最大长度
     * @throws IOException IO异常
     */
    public static void write(final Socket socket, final byte[] data, final int offset, final int length, final int maxLength) throws IOException {
        if (offset + length > data.length) {
            throw new IllegalArgumentException("offset+length");
        }
        // 写入个数
        int count = 0;
        // 写入位置平移量，会变
        int off = offset;
        OutputStream out = socket.getOutputStream();
        while (count < length) {
            int len = maxLength <= 0 ? length - count : Math.min(maxLength, length - count);
            out.write(data, off, len);
            off += len;
            count += len;
        }
    }
}
