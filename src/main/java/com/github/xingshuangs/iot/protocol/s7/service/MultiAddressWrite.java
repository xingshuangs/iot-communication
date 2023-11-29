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

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import com.github.xingshuangs.iot.utils.ByteUtil;
import com.github.xingshuangs.iot.utils.FloatUtil;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

import java.nio.charset.Charset;
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
        this.dataItems.add(DataItem.createReqByBoolean(data));
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
        this.dataItems.add(DataItem.createReqByByte(data));
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
        this.dataItems.add(DataItem.createReqByByte(data));
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

    /**
     * 添加字符串，针对非200smart的PLC
     *
     * @param address 地址
     * @param data    字符串数据
     * @return 对象本身
     */
    public MultiAddressWrite addString(String address, String data) {
        this.addStringCustom(address, data, 1);
        return this;
    }

    /**
     * 添加字符串，针对200smart的PLC
     *
     * @param address 地址
     * @param data    字符串数据
     * @return 对象本身
     */
    public MultiAddressWrite addStringIn200Smart(String address, String data) {
        this.addStringCustom(address, data, 0);
        return this;
    }

    /**
     * 自定义添加字符串
     *
     * @param address 地址
     * @param data    字符串数据
     * @param offset  偏移量
     */
    @SuppressWarnings("DuplicatedCode")
    private void addStringCustom(String address, String data, int offset) {
        byte[] dataBytes = data.getBytes(Charset.forName("GB2312"));
        byte[] tmp = new byte[1 + dataBytes.length];
        tmp[0] = ByteUtil.toByte(dataBytes.length);
        System.arraycopy(dataBytes, 0, tmp, 1, dataBytes.length);
        // 非200smart，字节索引+1
        RequestItem requestItem = AddressUtil.parseByte(address, tmp.length);
        requestItem.setByteAddress(requestItem.getByteAddress() + offset);
        this.requestItems.add(requestItem);
        this.dataItems.add(DataItem.createReqByByte(tmp));
    }
}
