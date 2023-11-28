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

package com.github.xingshuangs.iot.net;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 响应的回调
 *
 * @author xingshuang
 */
public class AckCallback<T> {

    /**
     * 响应数据
     */
    private T ackData;

    /**
     * 数据响应监听器
     */
    private final IAckListener<T> listener;

    /**
     * 是否完成
     */
    private boolean completed;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 超时时间ms
     */
    private final long timeoutMs;

    /**
     * 锁对象
     */
    private final Object lock = new Object();

    /**
     * 创建时间
     */
    private final LocalDateTime createTime = LocalDateTime.now();

    /**
     * 是否完成
     *
     * @return true：完成，false：未完成
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 获取异常消息
     *
     * @return 异常消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 获取超时时间，毫秒级
     *
     * @return 超时时间
     */
    public long getTimeoutMs() {
        return timeoutMs;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AckCallback() {
        this(null);
    }

    public AckCallback(IAckListener<T> listener) {
        this.ackData = null;
        this.completed = false;
        this.timeoutMs = 10_000;
        this.listener = listener;
    }

    /**
     * 成功响应
     *
     * @param ackPackage 响应数据包
     */
    public void responseOk(T ackPackage) {
        this.ackData = ackPackage;
        this.completed = true;
        this.errorMessage = "";
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
        if (this.listener != null) {
            CompletableFuture.runAsync(() -> this.listener.ok(ackPackage));
        }
    }

    /**
     * 失败响应
     *
     * @param message 错误消息
     */
    public void responseFail(String message) {
        this.ackData = null;
        this.completed = true;
        this.errorMessage = message;
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
        if (this.listener != null) {
            CompletableFuture.runAsync(() -> this.listener.fail(message));
        }
    }

    /**
     * 等待数据
     */
    public void waitData() {
        try {
            synchronized (this.lock) {
                this.lock.wait(this.timeoutMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 等待数据
     *
     * @param timeout 超时时间，毫秒级
     */
    public void waitData(long timeout) {
        try {
            synchronized (this.lock) {
                this.lock.wait(timeout);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
