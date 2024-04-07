/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        // 开启接收数据线程，远程的IP:{}，端口号：{}
        log.debug("Open the receiving thread, remote IP:{}, port number :{}", this.serverAddress.getAddress().getHostAddress(), this.serverAddress.getPort());
        while (!this.terminal) {
            try {
                byte[] data = this.read();
                log.debug("data length：{}", data.length);
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void triggerReceive() {
        CompletableFuture.runAsync(this::waitForReceiveData);
    }
}
