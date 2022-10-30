package com.github.xingshuangs.iot.protocol.common;


/**
 * 一个对象字节相关的接口
 *
 * @author xingshuang
 */
public interface IObjectByteArray {

    /**
     * 获取字节数组长度
     *
     * @return 长度结果
     */
    int byteArrayLength();

    /**
     * 转换为字节数组
     *
     * @return 字节数组
     */
    byte[] toByteArray();
}
