package com.github.xingshuangs.iot.net;


/**
 * 通信接口
 *
 * @author xingshuang
 */
public interface ICommunicable {

    void close();

    /**
     * 写入数据
     *
     * @param data 字节数组
     */
    void write(final byte[] data);

    /**
     * 写入数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     */
    void write(final byte[] data, final int offset, final int length);

    /**
     * 读取数据
     *
     * @param data 字节数组
     * @return 读取到的数据长度
     */
    int read(final byte[] data);


    /**
     * 读取数据
     *
     * @param data   字节数组
     * @param offset 偏移量
     * @param length 数据长度
     * @return 读取到的数据长度
     */
    int read(final byte[] data, final int offset, final int length);
}
