package com.github.xingshuangs.iot.protocol.rtsp.service;


import java.util.concurrent.CompletableFuture;

/**
 * RTSP的数据流
 *
 * @author xingshuang
 */
public interface IRtspDataStream {

    /**
     * 异步执行线程的future
     *
     * @return CompletableFuture
     */
    CompletableFuture<Void> getFuture();

    /**
     * 关闭
     */
    void close();

    /**
     * 触发接收数据
     */
    void triggerReceive();

    /**
     * 发送数据
     *
     * @param data 字节数组
     */
    void sendData(byte[] data);
}
