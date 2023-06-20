package com.github.xingshuangs.iot.net.client;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.ICommunicable;

import java.io.IOException;
import java.net.*;

/**
 * UDP客户端通信协议
 *
 * @author xingshuang
 */
public class UdpClientBasic implements ICommunicable {

    /**
     * 服务端地址
     */
    protected InetSocketAddress serverAddress;

    /**
     * socket对象
     */
    private DatagramSocket socket;

    /**
     * 获取本地端口号
     *
     * @return 本地端口号
     */
    public int getLocalPort() {
        DatagramSocket availableSocket = this.getAvailableSocket();
        return availableSocket.getLocalPort();
    }

    public UdpClientBasic() {
        this("127.0.0.1", 8088);
    }

    /**
     * 构造方法
     *
     * @param ip   服务端的IP地址
     * @param port 服务端的端口号
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
     * 绑定服务器
     *
     * @param ip   IP地址
     * @param port 端口号
     */
    public void bindServer(String ip, int port) {
        this.serverAddress = new InetSocketAddress(ip, port);
    }

    /**
     * 获取连接socket
     *
     * @return socket
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
     * 写入数据
     *
     * @param data 字节数组
     */
    public void write(final byte[] data) {
        this.write(data, 0, data.length, this.serverAddress);
    }


    /**
     * 写入数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     */
    public void write(final byte[] data, final int offset, final int length) {
        this.write(data, offset, length, this.serverAddress);
    }

    /**
     * 写入数据
     *
     * @param data    字节数组
     * @param offset  偏移量
     * @param length  数据长度
     * @param address socket地址
     */
    public void write(final byte[] data, final int offset, final int length, SocketAddress address) {
        DatagramPacket packet = new DatagramPacket(data, offset, length, address);
        this.write(packet);
    }

    /**
     * 写入数据
     *
     * @param packet DatagramPacket对象
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
     * 读取数据，完全不知道待接收的数据长度
     *
     * @return 字节数组
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
     * 读取数据
     *
     * @param data 字节数组
     * @return 读取到的数据长度
     */
    public int read(final byte[] data) {
        return this.read(data, 0, data.length);
    }


    /**
     * 读取数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     * @return 读取到的数据长度
     */
    public int read(final byte[] data, final int offset, final int length) {
        return this.read(data, offset, length, this.serverAddress);
    }

    /**
     * 读取数据
     *
     * @param data    字节数组
     * @param address socket地址
     * @return 读取到的数据长度
     */
    public int read(final byte[] data, final SocketAddress address) {
        return this.read(data, 0, data.length, address);
    }

    /**
     * 读取数据
     *
     * @param data    字节数组
     * @param offset  偏移量
     * @param length  数据长度
     * @param address socket地址
     * @return 读取到的数据长度
     */
    public int read(final byte[] data, final int offset, final int length, final SocketAddress address) {
        DatagramPacket packet = new DatagramPacket(data, offset, length, address);
        DatagramPacket res = this.read(packet);
        return res.getLength();
    }

    /**
     * 读取数据
     *
     * @param packet DatagramPacket数据对象
     * @return DatagramPacket数据对象
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
