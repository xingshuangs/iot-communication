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
     * 端口号
     */
    private final int port;

    /**
     * socket对象
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
     * 关闭
     */
    public void close() {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }
    }

    /**
     * 获取连接socket
     *
     * @return socket
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
     * 写入数据
     *
     * @param data    字节数组
     * @param address 地址
     */
    public void write(final byte[] data, SocketAddress address) {
        this.write(data, 0, data.length, address);
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
     * 读取数据
     *
     * @param data 字节数组
     * @return DatagramPacket数据对象
     */
    public DatagramPacket read(final byte[] data) {
        return this.read(data, 0, data.length);
    }

    /**
     * 读取数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     * @return DatagramPacket数据对象
     */
    public DatagramPacket read(final byte[] data, int offset, int length) {
        DatagramPacket packet = new DatagramPacket(data, offset, length);
        return this.read(packet);
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
