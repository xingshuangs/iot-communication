package com.github.xingshuangs.iot.net.socket;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author xingshuang
 */
public class SocketBasic {

    private Socket socket;

    private int timeout = 5000;

    private InetSocketAddress socketAddress;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

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
            this.dataInputStream.close();
            this.dataOutputStream.close();
            this.dataInputStream = null;
            this.dataOutputStream = null;
        }

        // 重新创建对象，并连接
        this.socket = new Socket();
        this.socket.connect(this.socketAddress, this.timeout);
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        return socket;
    }

    public boolean sendData(byte[] data) throws IOException {
        if (this.checkConnected()) {
            this.dataOutputStream.write(data);
            return true;
        } else {
            return false;
        }
    }
}
