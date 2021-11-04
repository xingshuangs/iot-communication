package com.github.xingshuangs.iot.net.socket;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author xingshuang
 */
@Slf4j
public class SocketBasic {

    private Socket socket;

    private int timeout = 5000;

    private InetSocketAddress socketAddress;

    private InputStream in;

    private OutputStream out;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public SocketBasic(String host, int port) {
        this.socketAddress = new InetSocketAddress(host, port);
    }

    private boolean checkConnected() {
        return this.socket != null && (socket.isConnected() && !socket.isClosed());
    }

    public Socket getAvailableSocket() throws IOException {

        // 已连接的直接返回socket
        if (this.checkConnected()) {
            return this.socket;
        }
        // 未连接，表示已断开，需要手动关闭socket，创建新的socket
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
            this.in.close();
            this.out.close();
            this.in = null;
            this.out = null;
        }

        // 重新创建对象，并连接
        this.socket = new Socket();
        this.socket.setSoTimeout(60_000);
        this.socket.connect(this.socketAddress, this.timeout);
        this.in = this.socket.getInputStream();
        this.out = this.socket.getOutputStream();
        return socket;
    }

    public void write(final byte[] data) throws IOException {
        this.out.write(data);
    }

    public void write(final byte[] data, final int offset, final int length) throws IOException {
        this.out.write(data, offset, length);
    }

    public int read(final byte[] b, int offset, int length) {
        int res = 0;
        try {
            int retry = 0;
            while ((this.in.available() <= 0) && (retry < 1000)) {
                if (retry > 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                retry++;
            }
            while ((this.in.available() > 0) && (length > 0)) {
                res = this.in.read(b, offset, length);
                offset += res;
                length -= res;
            }
            return res;
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }
}
