package com.github.xingshuangs.iot.net.socket;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;

import java.io.IOException;
import java.net.*;

/**
 * UDP客户端通信协议
 *
 * @author xingshuang
 */
public class UdpClientBasic {

    /**
     * 服务端地址
     */
    protected final InetSocketAddress serverAddress;

    /**
     * socket对象
     */
    private DatagramSocket socket;

    public UdpClientBasic() {
        this("127.0.0.1", 8088);
    }

    public UdpClientBasic(String ip, int port) {
        this.serverAddress = new InetSocketAddress(ip, port);
    }

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
        // 已连接的直接返回socket
        if (this.socket != null) {
            return this.socket;
        }
        try {
            // 重新创建对象，并连接
            this.socket = new DatagramSocket();
            // connect之后通信地址必须是这个地址
            this.socket.connect(this.serverAddress);
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
        this.write(data, 0, data.length);
    }

    /**
     * 写入数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     */
    public void write(final byte[] data, int offset, int length) {
        try {
            DatagramSocket availableSocket = this.getAvailableSocket();
            DatagramPacket packet = new DatagramPacket(data, offset, length);
            availableSocket.send(packet);
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
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
    public int read(final byte[] data, int offset, int length) {
        try {
            DatagramSocket availableSocket = this.getAvailableSocket();
            DatagramPacket packet = new DatagramPacket(data, offset, length, serverAddress);
            availableSocket.receive(packet);
            return packet.getLength();
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }

    }
}
