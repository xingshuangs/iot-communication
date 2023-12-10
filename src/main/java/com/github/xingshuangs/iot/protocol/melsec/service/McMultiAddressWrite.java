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

package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 多地址写
 *
 * @author xingshuang
 */
@Data
public class McMultiAddressWrite {

    /**
     * word列表
     */
    private final List<McDeviceContent> words = new ArrayList<>();

    /**
     * dword列表
     */
    private final List<McDeviceContent> dwords = new ArrayList<>();

//    /**
//     * 添加字节数据
//     *
//     * @param address 地址
//     * @param data    数据
//     * @return 对象本身
//     */
//    public McMultiAddressWrite addByte(String address, byte data) {
//        this.addByte(address, new byte[]{data});
//        return this;
//    }

//    /**
//     * 添加字节数组
//     *
//     * @param address 地址
//     * @param data    字节数组数据
//     * @return 对象本身
//     */
//    public McMultiAddressWrite addByte(String address, byte[] data) {
//        // 三菱1个字占2个字节
//        byte[] newData = data;
//        if (data.length % 2 != 0) {
//            newData = ByteWriteBuff.newInstance(data.length + 1, true).putBytes(data).getData();
//        }
//        // 软元件按字批量读取，是字的个数，而不是字节个数
//        McDeviceContent deviceContent = McDeviceContent.createBy(address, newData.length / 2, newData);
//        this.words.add(deviceContent);
//        return this;
//    }

    /**
     * 添加uint16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addUInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * 添加int16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addInt16(String address, short data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * 添加int16数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * 添加uint32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addUInt32(String address, long data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * 添加int32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addInt32(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * 添加float32数据
     *
     * @param address 地址
     * @param data    数据
     * @return 对象本身
     */
    public McMultiAddressWrite addFloat32(String address, float data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putFloat(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

//    /**
//     * 添加double数据
//     *
//     * @param address 地址
//     * @param data    数据
//     * @return 对象本身
//     */
//    public McMultiAddressWrite addFloat64(String address, double data) {
//        byte[] bytes = ByteWriteBuff.newInstance(8, EByteBuffFormat.AB_CD).putDouble(data).getData();
//        this.addByte(address, bytes);
//        return this;
//    }
//
//    /**
//     * 添加字符串
//     *
//     * @param address 地址
//     * @param data    字符串数据
//     * @return 对象本身
//     */
//    public McMultiAddressWrite addString(String address, String data) {
//        byte[] bytes = ByteWriteBuff.newInstance(data.length(), true).putString(data).getData();
//        this.addByte(address, bytes);
//        return this;
//    }
}
