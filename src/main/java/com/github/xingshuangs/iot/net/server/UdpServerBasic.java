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

package com.github.xingshuangs.iot.net.server;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * UDP 服务端通信基础类
 *
 * @author xingshuang
 */
public class UdpServerBasic {

    /**
     * Port Number.
     * (端口号)
     */
    private final int port;

    /**
     * Udp socket object.
     * (socket对象)
     */
    private DatagramSocket socket;

    public int getPort() {
        return port;
    }

    public UdpServerBasic() {
        this(8088);
    }

    public UdpServerBasic(int port) {
        this.port = port;
    }

    /**
     * Close UDP transport.
     * (关闭)
     */
    public void close() {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }
    }

    /**
     * Get connected socket.
     * (获取连接socket)
     *
     * @return socket
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public DatagramSocket getAvailableSocket() {
        // 已创建的直接返回socket
        if (this.socket != null) {
            return this.socket;
        }
        try {
            this.socket = new DatagramSocket(this.port);
            return this.socket;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data    byte array
     * @param address socket address.
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public void write(final byte[] data, SocketAddress address) {
        this.write(data, 0, data.length, address);
    }

    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data    byte array
     * @param offset  the start offset in the data.
     * @param length  the number of bytes to write.
     * @param address socket address.
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public void write(final byte[] data, final int offset, final int length, SocketAddress address) {
        DatagramPacket packet = new DatagramPacket(data, offset, length, address);
        this.write(packet);
    }

    /**
     * Write data by datagram packet.
     * (写入数据)
     *
     * @param packet DatagramPacket object
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public void write(DatagramPacket packet) {
        try {
            DatagramSocket availableSocket = this.getAvailableSocket();
            availableSocket.send(packet);
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 读取数据
     *
     * @param data 字节数组
     * @return DatagramPacket数据对象
     */
    public DatagramPacket read(final byte[] data) {
        return this.read(data, 0, data.length);
    }

    /**
     * Read data into DatagramPacket object
     * (读取数据)
     *
     * @param data   byte array
     * @param offset the start offset in the data.
     * @param length the number of bytes to read.
     * @return DatagramPacket object
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public DatagramPacket read(final byte[] data, int offset, int length) {
        DatagramPacket packet = new DatagramPacket(data, offset, length);
        return this.read(packet);
    }

    /**
     * Read data into DatagramPacket object
     * (读取数据)
     *
     * @param packet DatagramPacket object
     * @return DatagramPacket object
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public DatagramPacket read(DatagramPacket packet) {
        try {
            DatagramSocket availableSocket = this.getAvailableSocket();
            availableSocket.receive(packet);
            return packet;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }
}
