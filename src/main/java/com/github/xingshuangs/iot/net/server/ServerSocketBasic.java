package com.github.xingshuangs.iot.net.server;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * socket服务端的基础类
 *
 * @author xingshuang
 */
@Slf4j
public class ServerSocketBasic {

    /**
     * 服务器对象
     */
    private ServerSocket serverSocket;

    //region 服务端

    /**
     * 启动
     */
    public void start() {
        this.start(8088);
    }

    /**
     * 启动
     *
     * @param port 端口号
     */
    public void start(int port) {
        try {
            this.stop();
            this.serverSocket = new ServerSocket(port);
            CompletableFuture.runAsync(this::waitConnection);
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        try {
            if (this.isAlive()) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 是否活跃着
     *
     * @return ture：活跃着，false：死了
     */
    public boolean isAlive() {
        return this.serverSocket != null && !this.serverSocket.isClosed();
    }

    private void waitConnection() {
        while (this.isAlive()) {
            try {
                Socket client = this.serverSocket.accept();
                if (!this.checkClientValid(client)) {
                    client.close();
                }

                CompletableFuture.runAsync(() -> this.doAfterConnected(client));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    //endregion

    //region 客户端

    protected boolean checkClientValid(Socket client) {
        return true;
    }


    protected void doAfterConnected(Socket client) {
        log.debug("有客户端连入，IP[{}]，端口号[{}]", client.getInetAddress().getHostAddress(), client.getPort());

        while (SocketUtils.isConnected(client)) {
            try {
                InputStream in = client.getInputStream();
                int firstByte = in.read();
                if (firstByte == -1) {
                    client.close();
                    log.debug("客户端主动断开，IP[{}]，端口号[{}]", client.getInetAddress().getHostAddress(), client.getPort());
                    break;
                }
                byte[] data = new byte[in.available() + 1];
                data[0] = (byte) firstByte;
                int offset = 1;
                SocketUtils.read(client, data, offset, data.length - 1, 1024, -1);
                log.debug(new String(data));
            } catch (Exception e) {
                try {
                    if (!client.isClosed()) {
                        client.close();
                        break;
                    }
                } catch (Exception ex) {
                    // NOOP
                } finally {
                    log.debug("客户端异常断开，IP[{}]，端口号[{}]，原因：{}", client.getInetAddress().getHostAddress(), client.getPort(), e.getMessage());
                }
            }
        }
    }
    //endregion
}
