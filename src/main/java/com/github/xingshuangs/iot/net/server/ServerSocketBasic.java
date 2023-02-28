package com.github.xingshuangs.iot.net.server;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * socket服务端的基础类
 *
 * @author xingshuang
 */
//@Slf4j
public class ServerSocketBasic {

    /**
     * 服务器对象
     */
    private ServerSocket serverSocket;

    private ExecutorService executorService = new ThreadPoolExecutor(
            1,
            50,
            300L,
            TimeUnit.SECONDS,
            // 使用无缓冲队列,提交后立即执行
            new SynchronousQueue<>());

    public void start(int port) {
        try {
            this.stop();
            this.serverSocket = new ServerSocket(port);
            this.executorService.submit(this::waitConnection);
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    public void stop() {
        try {
            if (this.isConnected()) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    public boolean isConnected() {
        return this.serverSocket != null && !this.serverSocket.isClosed();
    }

    private void removeClient(Socket socket) {

    }

    private void waitConnection() {
        while (this.isConnected()) {
            try {
                Socket client = this.serverSocket.accept();
                if (!this.checkClientValid(client)) {
                    client.close();
                }
                this.executorService.submit(() -> this.doAfterConnected(client));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected boolean checkClientValid(Socket client) {
        return true;
    }

    protected boolean isClientConnected(Socket client){
        return client != null && (client.isConnected() && !client.isClosed());
    }

    protected void doAfterConnected(Socket client) {
        // NOOP
        while (this.isClientConnected(client)) {

        }
    }
}
