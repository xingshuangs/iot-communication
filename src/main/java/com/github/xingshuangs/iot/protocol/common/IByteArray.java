package com.github.xingshuangs.iot.protocol.common;


/**
 * @author xingshuang
 */
public interface IByteArray {

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
