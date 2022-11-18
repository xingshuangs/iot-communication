package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import com.github.xingshuangs.iot.utils.FloatUtil;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 多地址写
 *
 * @author xingshuang
 */
@Data
public class MultiAddressWrite {

    /**
     * 请求项列表
     */
    List<RequestItem> requestItems = new ArrayList<>();

    /**
     * 数据项列表
     */
    List<DataItem> dataItems = new ArrayList<>();

    /**
     * 添加boolean数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addBoolean(String address, boolean data) {
        this.requestItems.add(AddressUtil.parseBit(address));
        this.dataItems.add(DataItem.createByBoolean(data));
        return this;
    }

    /**
     * 添加字节数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addByte(String address, byte data) {
        this.requestItems.add(AddressUtil.parseByte(address, 1));
        this.dataItems.add(DataItem.createByByte(data));
        return this;
    }

    /**
     * 添加字节数组
     *
     * @param address 地址
     * @param data    字节数组数据
     * @return 对象本身
     */
    public MultiAddressWrite addByte(String address, byte[] data) {
        this.requestItems.add(AddressUtil.parseByte(address, data.length));
        this.dataItems.add(DataItem.createByByte(data));
        return this;
    }

    /**
     * 添加uint16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addUInt16(String address, int data) {
        byte[] bytes = ShortUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加int16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addInt16(String address, short data) {
        byte[] bytes = ShortUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加int16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addInt16(String address, int data) {
        byte[] bytes = ShortUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加uint32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addUInt32(String address, long data) {
        byte[] bytes = IntegerUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加int32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addInt32(String address, int data) {
        byte[] bytes = IntegerUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加float32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addFloat32(String address, float data) {
        byte[] bytes = FloatUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }

    /**
     * 添加double数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public MultiAddressWrite addFloat64(String address, double data) {
        byte[] bytes = FloatUtil.toByteArray(data);
        this.addByte(address, bytes);
        return this;
    }
}
