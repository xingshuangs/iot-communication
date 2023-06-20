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
     * @param timeout   超时时间
     * @return 读取数量
     * @throws IOException IO异常
     */
    public static int read(final Socket socket, final byte[] data, final int offset, final int length, final int maxLength, final int timeout) throws IOException {
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
                throw new SocketRuntimeException("读取数据异常，未读取到数据");
            }
            count += num;
            off += num;
            // 比读取的数据长度还小，则证明已经读取完了
            if (num < len) {
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
