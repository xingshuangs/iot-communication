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

package com.github.xingshuangs.iot.net.client;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.ICommunicable;

import java.io.IOException;
import java.net.*;

import static com.github.xingshuangs.iot.common.constant.GeneralConst.LOCALHOST;

/**
 * UDP client basic class.
 *
 * @author xingshuang
 */
public class UdpClientBasic implements ICommunicable {

    /**
     * Server address.
     * (服务端地址)
     */
    protected InetSocketAddress serverAddress;

    /**
     * UDP socket object.
     * (socket对象)
     */
    protected DatagramSocket socket;

    /**
     * Get local port number
     * (获取本地端口号)
     *
     * @return local port number
     */
    public int getLocalPort() {
        DatagramSocket availableSocket = this.getAvailableSocket();
        return availableSocket.getLocalPort();
    }

    public UdpClientBasic() {
        this(LOCALHOST, 8088);
    }

    /**
     * Construction.
     *
     * @param ip   server ip address
     * @param port Server port number
     */
    public UdpClientBasic(String ip, int port) {
        this.serverAddress = new InetSocketAddress(ip, port);
    }

    public void close() {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }
    }

    /**
     * Bind server.
     * (绑定服务器)
     *
     * @param ip   IP address
     * @param port port number
     */
    public void bindServer(String ip, int port) {
        this.serverAddress = new InetSocketAddress(ip, port);
    }

    /**
     * Get connected socket.
     * (获取连接socket)
     *
     * @return socket
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public DatagramSocket getAvailableSocket() {
        // 已连接的直接返回socket
        if (this.socket != null) {
            return this.socket;
        }
        try {
            // 重新创建对象，并连接
            this.socket = new DatagramSocket();
            // connect之后通信地址必须是这个地址
//            this.socket.connect(this.serverAddress);
            return socket;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data byte array
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public void write(final byte[] data) {
        this.write(data, 0, data.length, this.serverAddress);
    }


    /**
     * Write data by byte array.
     * (写入数据)
     *
     * @param data   byte array
     * @param offset the start offset in the data.
     * @param length the number of bytes to write.
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public void write(final byte[] data, final int offset, final int length) {
        this.write(data, offset, length, this.serverAddress);
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
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @return byte array
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public byte[] read() {
        byte[] buffer = new byte[4096];
        int length = this.read(buffer);
        if (length < 4096) {
            byte[] data = new byte[length];
            System.arraycopy(buffer, 0, data, 0, length);
            return data;
        } else {
            return buffer;
        }
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data byte array
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data) {
        return this.read(data, 0, data.length);
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data    byte array
     * @param timeout timeout in millisecond，0：blocked，big than 0：timeout time.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final int timeout) {
        return this.read(data, 0, data.length, timeout);
    }


    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data   byte array
     * @param offset the start offset in the data.
     * @param length the number of bytes to read.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final int offset, final int length) {
        return this.read(data, offset, length, this.serverAddress, 0);
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data    byte array
     * @param offset  the start offset in the data.
     * @param length  the number of bytes to read.
     * @param timeout timeout in millisecond，0：blocked，big than 0：timeout time.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final int offset, final int length, final int timeout) {
        return this.read(data, offset, length, this.serverAddress, timeout);
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data    byte array
     * @param address socket address.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final SocketAddress address) {
        return this.read(data, 0, data.length, address, 0);
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data    byte array
     * @param address socket address.
     * @param timeout timeout in millisecond，0：blocked，big than 0：timeout time.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final SocketAddress address, final int timeout) {
        return this.read(data, 0, data.length, address, timeout);
    }

    /**
     * Read data and store it in the position of the specified byte array.
     * (读取数据)
     *
     * @param data    byte array
     * @param offset  the start offset in the data.
     * @param length  the number of bytes to read.
     * @param address socket address.
     * @param timeout timeout in millisecond，0：blocked，big than 0：timeout time.
     * @return the total number of bytes read into the data
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public int read(final byte[] data, final int offset, final int length, final SocketAddress address, final int timeout) {
        DatagramPacket packet = new DatagramPacket(data, offset, length, address);
        DatagramPacket res = this.read(packet, timeout);
        return res.getLength();
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
        return this.read(packet, 0);
    }

    /**
     * Read data into DatagramPacket object
     * (读取数据)
     *
     * @param packet  DatagramPacket object
     * @param timeout timeout in millisecond，0：blocked，big than 0：timeout time.
     * @return DatagramPacket object
     * @throws SocketRuntimeException Socket Runtime Exception
     */
    public DatagramPacket read(DatagramPacket packet, final int timeout) {
        try {
            DatagramSocket availableSocket = this.getAvailableSocket();
            availableSocket.setSoTimeout(timeout);
            availableSocket.receive(packet);
            return packet;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }
}
