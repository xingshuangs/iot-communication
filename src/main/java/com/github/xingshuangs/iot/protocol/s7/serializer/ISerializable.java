package com.github.xingshuangs.iot.protocol.s7.serializer;


/**
 * S7序列化接口
 *
 * @author xingshuang
 */
public interface ISerializable {

    /**
     * 读数据
     *
     * @param targetClass
     * @param <T>
     * @return
     */
    <T> T read(Class<T> targetClass);

    /**
     * 写数据
     *
     * @param targetBean
     * @param <T>
     */
    <T> void write(T targetBean);
}
