package com.github.xingshuangs.iot.net;


/**
 * 响应监听器
 *
 * @author xingshuang
 */
public interface IAckListener<T> {

    /**
     * 成功执行业务
     *
     * @param ack 响应数据
     */
    void ok(T ack);

    /**
     * 失败执行业务
     *
     * @param message 错误消息
     */
    void fail(String message);
}
