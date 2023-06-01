package com.github.xingshuangs.iot.net.client;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.ICommunicable;
import com.github.xingshuangs.iot.net.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xingshuang
 */
@Slf4j
public class TcpClientBasic implements ICommunicable {

    // region 私有对象

    /**
     * socket对象
     */
    private Socket socket;

    /**
     * 连接超时时间，默认是10s
     */
    private int connectTimeout = 10_000;

    /**
     * 接收数据超时时间，默认是10s
     */
    private int receiveTimeout = 10_000;

    /**
     * socket的地址
     */
    protected final InetSocketAddress socketAddress;

    /**
     * socket是否发生错误
     */
    private final AtomicBoolean socketError;

    /**
     * 自动重连，true:自动重连，false：不自动重连，默认自动重连
     */
    protected boolean enableReconnect = true;

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public boolean isEnableReconnect() {
        return enableReconnect;
    }

    public void setEnableReconnect(boolean enableReconnect) {
        this.enableReconnect = enableReconnect;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(int receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    // endregion

    // region 构造方法
    public TcpClientBasic() {
        this("127.0.0.1", 8088);
    }

    public TcpClientBasic(String host, int port) {
        this.socketError = new AtomicBoolean(false);
        this.socketAddress = new InetSocketAddress(host, port);
    }
    // endregion

    //region 公共方法

    /**
     * 校验连接状态，true为连接，false为断开
     *
     * @return 连接状态，true为连接，false为断开
     */
    public boolean checkConnected() {
        return !this.socketError.get() && SocketUtils.isConnected(this.socket);
    }

    /**
     * 连接
     */
    public void connect() {
        this.close();
        this.getAvailableSocket();
    }

    /**
     * 获取有效的socket对象
     *
     * @return socket对象
     */
    public Socket getAvailableSocket() {
        // socket连接过了，同时又不支持自动重连，直接返回
        if (this.socket != null && !this.enableReconnect) {
            return this.socket;
        }

        // 已连接的直接返回socket
        if (this.checkConnected()) {
            return this.socket;
        }
        // 未连接，表示已断开，需要手动关闭socket，创建新的socket
        this.close();

        try {
            // 重新创建对象，并连接
            this.socket = new Socket();
            this.socket.setSoTimeout(this.receiveTimeout);
            this.socket.connect(this.socketAddress, this.connectTimeout);
            this.socketError.set(false);
            log.debug("创建并连接服务端[{}]成功", this.socketAddress);
            this.doAfterConnected();
            return socket;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 关闭socket
     */
    public void close() {
        try {
            SocketUtils.close(this.socket);
            this.socket = null;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    //endregion

    /**
     * 连接成功之后要做的动作
     */
    protected void doAfterConnected() {
        // NOOP
    }

    //region 读写方法

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
    public void write(final byte[] data, final int offset, final int length) {
        this.write(data, offset, length, -1);
    }

    /**
     * 写入数据
     *
     * @param data      字节数组
     * @param offset    偏移量
     * @param length    数据长度
     * @param maxLength 单次通信允许的对最大长度
     */
    public void write(final byte[] data, final int offset, final int length, final int maxLength) {
        try {
            Socket availableSocket = this.getAvailableSocket();
            SocketUtils.write(availableSocket, data, offset, length, maxLength);
        } catch (IOException e) {
            this.socketError.set(true);
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 读取数据
     *
     * @param data 字节数组
     * @return 读取的数据长度
     */
    public int read(final byte[] data) {
        return this.read(data, 0, data.length, this.receiveTimeout);
    }

    /**
     * 读取数据
     *
     * @param data    字节数组
     * @param timeout 超时时间，毫秒级别
     * @return 读取的数据长度
     */
    public int read(final byte[] data, final int timeout) {
        return this.read(data, 0, data.length, timeout);
    }

    /**
     * 读取数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     * @return 读取的数据长度
     */
    public int read(final byte[] data, final int offset, final int length) {
        return this.read(data, offset, length, this.receiveTimeout);
    }

    /**
     * 读取数据
     *
     * @param data    字节数组
     * @param offset  偏移量
     * @param length  数据长度
     * @param timeout 超时时间，毫秒级别
     * @return 读取的数据长度
     */
    public int read(final byte[] data, final int offset, final int length, final int timeout) {
        return this.read(data, offset, length, -1, timeout);
    }

    /**
     * 读取数据
     *
     * @param data      字节数组
     * @param offset    偏移量
     * @param length    数据长度
     * @param timeout   超时时间，毫秒级别
     * @param maxLength 单次通信允许的对最大长度
     * @return 读取的数据长度
     */
    public int read(final byte[] data, final int offset, final int length, final int maxLength, final int timeout) {
        try {
            Socket availableSocket = this.getAvailableSocket();
            return SocketUtils.read(availableSocket, data, offset, length, maxLength, timeout);
        } catch (Exception e) {
            this.socketError.set(true);
            throw new SocketRuntimeException(e);
        }
    }

    //endregion
}
