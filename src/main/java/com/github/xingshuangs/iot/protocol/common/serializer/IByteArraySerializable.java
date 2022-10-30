package com.github.xingshuangs.iot.protocol.common.serializer;


/**
 * 字节数组的序列化接口
 *
 * @author xingshuang
 */
public interface IByteArraySerializable {

    /**
     * 读数据
     *
     * @param targetClass 目标类
     * @param src         字节数组
     * @param <T>         类型
     * @return 类型对象
     */
    <T> T toObject(Class<T> targetClass, byte[] src);

    /**
     * 写数据
     *
     * @param targetBean 目标对象
     * @param <T>        类型
     * @return 字节数组
     */
    <T> byte[] toByteArray(T targetBean);
}
