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
 * Multi address write.
 * 多address string写
 *
 * @author xingshuang
 */
@Data
public class McMultiAddressWrite {

    /**
     * word list.
     */
    private final List<McDeviceContent> words = new ArrayList<>();

    /**
     * dword list.
     */
    private final List<McDeviceContent> dwords = new ArrayList<>();

//    /**
//     * 添加字节data
//     *
//     * @param address address string
//     * @param data    data
//     * @return this object
//     */
//    public McMultiAddressWrite addByte(String address, byte data) {
//        this.addByte(address, new byte[]{data});
//        return this;
//    }

//    /**
//     * 添加字节数组
//     *
//     * @param address address string
//     * @param data    字节数组data
//     * @return this object
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
     * Add uint16 data.
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addUInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * Add int16 data
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addInt16(String address, short data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * Add int16 data
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.words.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * Add uint32 data
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addUInt32(String address, long data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * Add int32 data
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addInt32(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

    /**
     * Add float32 data
     *
     * @param address address string
     * @param data    data
     * @return this object
     */
    public McMultiAddressWrite addFloat32(String address, float data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putFloat(data).getData();
        this.dwords.add(McDeviceContent.createBy(address, bytes));
        return this;
    }

//    /**
//     * Add double data
//     *
//     * @param address address string
//     * @param data    data
//     * @return this object
//     */
//    public McMultiAddressWrite addFloat64(String address, double data) {
//        byte[] bytes = ByteWriteBuff.newInstance(8, EByteBuffFormat.AB_CD).putDouble(data).getData();
//        this.addByte(address, bytes);
//        return this;
//    }
//
//    /**
//     * Add string
//     *
//     * @param address address string
//     * @param data    字符串data
//     * @return this object
//     */
//    public McMultiAddressWrite addString(String address, String data) {
//        byte[] bytes = ByteWriteBuff.newInstance(data.length(), true).putString(data).getData();
//        this.addByte(address, bytes);
//        return this;
//    }
}
