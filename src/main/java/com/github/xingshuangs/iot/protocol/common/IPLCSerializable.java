package com.github.xingshuangs.iot.protocol.common;


/**
 * PLC序列化接口
 *
 * @author xingshuang
 */
public interface IPLCSerializable {

    /**
     * 读数据
     *
     * @param targetClass 目标类
     * @param <T>         类型
     * @return 类型对象
     */
    <T> T read(Class<T> targetClass);

    /**
     * 写数据
     *
     * @param targetBean 目标对象
     * @param <T>        类型
     */
    <T> void write(T targetBean);
}
