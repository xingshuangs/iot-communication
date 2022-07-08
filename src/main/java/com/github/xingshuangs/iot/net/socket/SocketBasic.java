package com.github.xingshuangs.iot.net.socket;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xingshuang
 */
@Slf4j
public class SocketBasic {

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
    private InetSocketAddress socketAddress;

    /**
     * socket是否发生错误
     */
    private AtomicBoolean socketError;

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
    public SocketBasic() {
        this("127.0.0.1", 8088);
    }

    public SocketBasic(String host, int port) {
        this.socketError = new AtomicBoolean(false);
        this.socketAddress = new InetSocketAddress(host, port);
//        this.executorService = Executors.newSingleThreadExecutor();
    }
    // endregion

    //region 私有方法

    /**
     * 校验连接状态，true为连接，false为断开
     *
     * @return 连接状态，true为连接，false为断开
     */
    private boolean checkConnected() {
        return !this.socketError.get() && this.socket != null && (socket.isConnected() && !socket.isClosed());
    }

    /**
     * 获取有效的socket对象
     *
     * @return socket对象
     */
    private Socket getAvailableSocket() {

        // 已连接的直接返回socket
        if (this.checkConnected()) {
            return this.socket;
        }
        // 未连接，表示已断开，需要手动关闭socket，创建新的socket
        this.close();

        try {
            // 重新创建对象，并连接
            this.socket = new Socket();
//        this.socket.setTcpNoDelay(true);
            this.socket.setSoTimeout(10_000);
            this.socket.connect(this.socketAddress, this.connectTimeout);
            this.socketError.set(false);
            this.doAfterConnected();
//            log.debug("实例化一个新的socket对象");
            return socket;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    public void close() {
        try {
            if (this.socket != null) {
                this.socket.close();
                this.socket = null;
            }
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

    //region 公有方法

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
            Socket availableSocket = this.getAvailableSocket();
            OutputStream out = availableSocket.getOutputStream();
            out.write(data, offset, length);
        } catch (IOException e) {
            this.socketError.set(true);
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 写入字节
     *
     * @param data      字节数组
     * @param maxLength 最大允许写入长度
     */
    public void writeCycle(final byte[] data, int maxLength) {
        int offset = 0;
        while (offset < data.length) {
            int length = maxLength <= 0 ? data.length - offset : Math.min(maxLength, data.length - offset);
            this.write(data, offset, length);
            offset += length;
        }
    }

    /**
     * 写入字节
     *
     * @param data 字节数组
     */
    public void writeCycle(final byte[] data) {
        this.writeCycle(data, -1);
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
    public int read(final byte[] data, int timeout) {
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
    public int read(final byte[] data, int offset, int length) {
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
    public int read(final byte[] data, int offset, int length, int timeout) {
        try {
            Socket availableSocket = this.getAvailableSocket();
            // 阻塞不是指read的时间长短，可以理解为没有数据可读，线程一直在这等待
            availableSocket.setSoTimeout(timeout);
            InputStream in = availableSocket.getInputStream();
            int count = in.read(data, offset, length);
            if (count < 0) {
                throw new SocketRuntimeException("读取数据异常，未读取到数据");
            }
            return count;
        } catch (Exception e) {
            this.socketError.set(true);
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 读取字节
     *
     * @param data      字节数组
     * @param maxLength 最大允许读取长度
     * @return 读取数量
     */
    public int readCycle(final byte[] data, int maxLength) {
        int offset = 0;
        while (offset < data.length) {
            int length = maxLength <= 0 ? data.length - offset : Math.min(maxLength, data.length - offset);
            this.read(data, offset, length);
            offset += length;
        }
        return offset;
    }

    /**
     * 读取字节
     *
     * @param data 字节数组
     * @return 读取数量
     */
    public int readCycle(final byte[] data) {
        return this.readCycle(data, -1);
    }


    //endregion
}
