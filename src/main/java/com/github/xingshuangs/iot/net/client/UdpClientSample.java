package com.github.xingshuangs.iot.net.client;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 简单UDP示例
 *
 * @author xingshuang
 */
@Slf4j
public class UdpClientSample extends UdpClientBasic {

    /**
     * 是否终止线程
     */
    private boolean terminal = false;

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<byte[]> commCallback;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public UdpClientSample() {
    }

    public UdpClientSample(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void close() {
        this.terminal = true;
        super.close();
    }

    private void waitForReceiveData() {
        log.debug("开启接收数据线程，远程的IP:{}，端口号：{}", this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.getReceiveData();
                log.debug("数据长度：{}", data.length);
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取接收的数据
     *
     * @return 字节数组
     */
    private byte[] getReceiveData() {
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int length = this.read(buffer);
//        log.debug("接收到数据长度为：{}，缓存总长度：{}", length, bufferSize);
        if (length < bufferSize) {
            byte[] data = new byte[length];
            System.arraycopy(buffer, 0, data, 0, length);
            return data;
        } else {
            return buffer;
        }
    }

    public void triggerReceive() {
        CompletableFuture.runAsync(this::waitForReceiveData);
    }
}
