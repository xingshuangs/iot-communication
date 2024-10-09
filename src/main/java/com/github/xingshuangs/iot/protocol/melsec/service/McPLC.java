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


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import com.github.xingshuangs.iot.protocol.melsec.model.McFrame1EAccessRoute;
import com.github.xingshuangs.iot.protocol.melsec.model.McFrame4E3EAccessRoute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.xingshuangs.iot.common.constant.GeneralConst.LOCALHOST;
import static com.github.xingshuangs.iot.common.constant.GeneralConst.MELSEC_PORT;

/**
 * 三菱的PLC
 * 通信帧命名规格<br>
 * 通信帧命名格式如下：<br>
 * xxx 兼容 n m 帧(示例: QnA 兼容 3C 帧、QnA 兼容 3E 帧)<br>
 * 1、xxx 用于表示与以前产品模块的指令兼容性的对象可编程控制器 CPU<br>
 * A : A 系列可编程控制器 CPU<br>
 * QnA : QnA 系列可编程控制器 CPU<br>
 * 2、n对应的以前产品模块的帧<br>
 * 1 : 兼容 A 系列的计算机链接模块、以太网接口模块支持的指令的通信帧<br>
 * 2 : 兼容 QnA 系列串行通信模块支持的 QnA 简易帧<br>
 * 3 : QnA 系列串行通信模块支持的 QnA 帧及兼容 QnA 系列以太网接口模块支持的通信帧<br>
 * 4 : 兼容 QnA 系列串行通信模块支持的 QnA 扩展帧<br>
 * 3、m是指相应帧进行数据通信的对象模块<br>
 * C : C24<br>
 * E : E71<br>
 * <p>
 * 通信方式<br>
 * 一般我们使用比较多的是以太网通信，<br>
 * 对于FX5U系列/Q系列/Qna系列/L系列的PLC，通常会使用QnA兼容3E帧，<br>
 * 对于FX3U系列，我们需要加以太网模块，采用A兼容1E帧。<br>
 * 对于串口设备，一般会使用QnA兼容2C帧和QnA兼容4C帧。<br>
 *
 * @author xingshuang
 */
public class McPLC extends McNetwork {

    public McPLC() {
        this(EMcSeries.QnA, EMcSeries.QnA.getFrameType(), LOCALHOST, MELSEC_PORT);
    }

    public McPLC(String host, int port) {
        this(EMcSeries.QnA, EMcSeries.QnA.getFrameType(), host, port);
    }

    public McPLC(EMcSeries series, String host, int port) {
        this(series, series.getFrameType(), host, port);
    }

    public McPLC(EMcSeries series, EMcFrameType frameType, String host, int port) {
        super(host, port);
        this.tag = "Melsec";
        this.series = series;
        this.frameType = frameType;
        this.accessRoute = frameType == EMcFrameType.FRAME_1E ? McFrame1EAccessRoute.createDefault() : McFrame4E3EAccessRoute.createDefault();
    }

    //region 软元件读取

    /**
     * Read multi address
     * (读多地址)
     *
     * @param multiAddressRead multi address
     * @return device content list
     */
    public List<McDeviceContent> readMultiAddress(McMultiAddressRead multiAddressRead) {
        return this.readDeviceRandomInWord(multiAddressRead.getWords(), multiAddressRead.getDwords());
    }

    /**
     * Read boolean.
     * (读取booleans数据)
     *
     * @param address address
     * @param count   boolean count
     * @return boolean list
     */
    public List<Boolean> readBoolean(String address, int count) {
        // 三菱1个字节对应两个boolean
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInBit(deviceAddress);
        List<Boolean> res = this.getBooleansBy(deviceContent.getData());
        return res.subList(0, count);
    }

    /**
     * Read bytes.
     * (读取字节数组数据)
     *
     * @param address address string
     * @param count   byte count
     * @return byte array
     */
    public byte[] readBytes(String address, int count) {
        // 三菱1个字占2个字节
        int newCount = count % 2 == 0 ? (count / 2) : ((count + 1) / 2);
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, newCount);
        McDeviceContent deviceContent = this.readDeviceBatchInWord(deviceAddress);
        return ByteReadBuff.newInstance(deviceContent.getData()).getBytes(count);
    }

    /**
     * Read boolean.
     * (读取1个boolean数据)
     *
     * @param address address string
     * @return boolean data
     */
    public boolean readBoolean(String address) {
        List<Boolean> booleans = this.readBoolean(address, 1);
        return booleans.get(0);
    }

    /**
     * Read byte.
     * (读取1个字节数据)
     *
     * @param address address string
     * @return byte data
     */
    public byte readByte(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return bytes[0];
    }

    /**
     * Read int16.
     * (读取1个Int16数据)
     *
     * @param address address string
     * @return short data
     */
    public short readInt16(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getInt16();
    }

    /**
     * Read int16.
     * (读取多个Int16数据)
     *
     * @param addresses address string
     * @return short list
     */
    public List<Short> readInt16(String... addresses) {
        return this.readInt16(Arrays.asList(addresses));
    }

    /**
     * Read int16.
     * (读取多个Int16数据)
     *
     * @param addresses address string
     * @return short list
     */
    public List<Short> readInt16(List<String> addresses) {
        List<McDeviceAddress> deviceAddresses = addresses.stream().map(McDeviceAddress::createBy)
                .collect(Collectors.toList());

        List<McDeviceContent> deviceContents = this.readDeviceRandomInWord(deviceAddresses, new ArrayList<>());
        return deviceContents.stream()
                .map(x -> ByteReadBuff.newInstance(x.getData(), true).getInt16())
                .collect(Collectors.toList());
    }

    /**
     * Read uint16.
     * (读取1个UInt16数据)
     *
     * @param address address string
     * @return UInt16
     */
    public int readUInt16(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getUInt16();
    }

    /**
     * Read uint16.
     * (读取多个UInt16数据)
     *
     * @param addresses addresses string
     * @return integer list
     */
    public List<Integer> readUInt16(String... addresses) {
        return this.readUInt16(Arrays.asList(addresses));
    }

    /**
     * Read uint16.
     * (读取多个UInt16数据)
     *
     * @param addresses addresses string
     * @return integer list
     */
    public List<Integer> readUInt16(List<String> addresses) {
        List<McDeviceAddress> deviceAddresses = addresses.stream().map(McDeviceAddress::createBy)
                .collect(Collectors.toList());

        List<McDeviceContent> deviceContents = this.readDeviceRandomInWord(deviceAddresses, new ArrayList<>());

        return deviceContents.stream()
                .map(x -> ByteReadBuff.newInstance(x.getData(), true).getUInt16())
                .collect(Collectors.toList());
    }

    /**
     * Read int32.
     * (读取1个Int32数据)
     *
     * @param address address string
     * @return int data
     */
    public int readInt32(String address) {
        byte[] bytes = this.readBytes(address, 4);
        return ByteReadBuff.newInstance(bytes, EByteBuffFormat.AB_CD).getInt32();
    }

    /**
     * Read int32.
     * (读取多个Int32数据)
     *
     * @param addresses addresses string
     * @return integer list
     */
    public List<Integer> readInt32(String... addresses) {
        return this.readInt32(Arrays.asList(addresses));
    }

    /**
     * Read int32.
     * (读取多个Int32数据)
     *
     * @param addresses addresses string
     * @return integer list
     */
    public List<Integer> readInt32(List<String> addresses) {
        List<McDeviceAddress> deviceAddresses = addresses.stream().map(McDeviceAddress::createBy)
                .collect(Collectors.toList());

        List<McDeviceContent> deviceContents = this.readDeviceRandomInWord(new ArrayList<>(), deviceAddresses);

        return deviceContents.stream()
                .map(x -> ByteReadBuff.newInstance(x.getData(), EByteBuffFormat.AB_CD).getInt32())
                .collect(Collectors.toList());
    }

    /**
     * Read uint32.
     * (读取1个UInt32数据)
     *
     * @param address address string
     * @return long data
     */
    public long readUInt32(String address) {
        byte[] bytes = this.readBytes(address, 4);
        return ByteReadBuff.newInstance(bytes, EByteBuffFormat.AB_CD).getUInt32();
    }

    /**
     * Read uint32.
     * (读取多个UInt32数据)
     *
     * @param addresses addresses string
     * @return long list
     */
    public List<Long> readUInt32(String... addresses) {
        return this.readUInt32(Arrays.asList(addresses));
    }

    /**
     * Read uint32.
     * (读取多个UInt32数据)
     *
     * @param addresses addresses string
     * @return long list
     */
    public List<Long> readUInt32(List<String> addresses) {
        List<McDeviceAddress> deviceAddresses = addresses.stream().map(McDeviceAddress::createBy)
                .collect(Collectors.toList());

        List<McDeviceContent> deviceContents = this.readDeviceRandomInWord(new ArrayList<>(), deviceAddresses);

        return deviceContents.stream()
                .map(x -> ByteReadBuff.newInstance(x.getData(), EByteBuffFormat.AB_CD).getUInt32())
                .collect(Collectors.toList());
    }

    /**
     * Read float32.
     * (读取1个Float32数据)
     *
     * @param address address string
     * @return float list
     */
    public float readFloat32(String address) {
        byte[] bytes = this.readBytes(address, 4);
        return ByteReadBuff.newInstance(bytes, EByteBuffFormat.AB_CD).getFloat32();
    }

    /**
     * Read float32.
     * (读取多个Float32数据)
     *
     * @param addresses addresses string
     * @return Float list
     */
    public List<Float> readFloat32(String... addresses) {
        return this.readFloat32(Arrays.asList(addresses));
    }

    /**
     * Read float32.
     * (读取多个Float32数据)
     *
     * @param addresses addresses string
     * @return Float list
     */
    public List<Float> readFloat32(List<String> addresses) {
        List<McDeviceAddress> deviceAddresses = addresses.stream().map(McDeviceAddress::createBy)
                .collect(Collectors.toList());

        List<McDeviceContent> deviceContents = this.readDeviceRandomInWord(new ArrayList<>(), deviceAddresses);

        return deviceContents.stream()
                .map(x -> ByteReadBuff.newInstance(x.getData(), EByteBuffFormat.AB_CD).getFloat32())
                .collect(Collectors.toList());
    }

    /**
     * Read float64.
     * (读取1个Float64数据)
     *
     * @param address address string
     * @return double data
     */
    public double readFloat64(String address) {
        byte[] bytes = this.readBytes(address, 8);
        return ByteReadBuff.newInstance(bytes, EByteBuffFormat.AB_CD).getFloat64();
    }

//    /**
//     * 读取多个Float64数据
//     *
//     * @param addresses addresses
//     * @return Double列表
//     */
//    public List<Double> readFloat64(String... addresses) {
//        return this.readFloat64(Arrays.asList(addresses));
//    }
//
//    /**
//     * 读取多个Float64数据
//     *
//     * @param addresses addresses
//     * @return Double列表
//     */
//    public List<Double> readFloat64(List<String> addresses) {
//        List<McDeviceAddress> deviceAddresses = addresses.stream().map(x -> McDeviceAddress.createBy(x, 4))
//                .collect(Collectors.toList());
//
//        List<McDeviceContent> deviceContents = this.readDeviceBatchMultiBlocks(deviceAddresses, new ArrayList<>());
//
//        return deviceContents.stream()
//                .map(x -> ByteReadBuff.newInstance(x.getData(), EByteBuffFormat.AB_CD).getFloat64())
//                .collect(Collectors.toList());
//    }

    /**
     * Read string.
     * (读取字符串数据)
     *
     * @param address address string
     * @param length  string length
     * @return string
     */
    public String readString(String address, int length) {
        byte[] bytes = this.readBytes(address, length);
        return ByteReadBuff.newInstance(bytes, true).getString(length);
    }

    //endregion

    //region 软元件写入

    /**
     * Write multi address.
     * (写多地址)
     *
     * @param multiAddressWrite addresses
     */
    public void writeMultiAddress(McMultiAddressWrite multiAddressWrite) {
        this.writeDeviceRandomInWord(multiAddressWrite.getWords(), multiAddressWrite.getDwords());
    }

    /**
     * Write boolean.
     * (写入多个boolean)
     *
     * @param address  address string
     * @param booleans boolean data
     */
    public void writeBoolean(String address, Boolean... booleans) {
        this.writeBoolean(address, Arrays.asList(booleans));
    }

    /**
     * Write boolean.
     * (写入boolean数据列表)
     *
     * @param address  address string
     * @param booleans boolean list
     */
    public void writeBoolean(String address, List<Boolean> booleans) {
        byte[] bytes = this.getBytesBy(booleans);
        McDeviceContent deviceContent = McDeviceContent.createBy(address, booleans.size(), bytes);
        this.writeDeviceBatchInBit(deviceContent);
    }

    /**
     * Write bytes.
     * (写入字节数组)
     *
     * @param address address string
     * @param data    byte array data
     */
    public void writeBytes(String address, byte[] data) {
        // 三菱1个字占2个字节
        byte[] newData = data;
        if (data.length % 2 != 0) {
            newData = ByteWriteBuff.newInstance(data.length + 1, true).putBytes(data).getData();
        }
        // 软元件按字批量读取，是字的个数，而不是字节个数
        McDeviceContent deviceContent = McDeviceContent.createBy(address, newData.length / 2, newData);
        this.writeDeviceBatchInWord(deviceContent);
    }

    /**
     * Write boolean.
     * (写入1个boolean数据)
     *
     * @param address address string
     * @param data    boolean data
     */
    public void writeBoolean(String address, boolean data) {
        this.writeBoolean(address, Collections.singletonList(data));
    }

    /**
     * Write byte.
     * (写入1个字节数据)
     *
     * @param address address string
     * @param data    byte data
     */
    public void writeByte(String address, byte data) {
        this.writeBytes(address, new byte[]{data});
    }

    /**
     * Write int16.
     * (写入1个Int16数据)
     *
     * @param address address string
     * @param data    short data
     */
    public void writeInt16(String address, short data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write int16.
     * (写入多个Int16数据)
     *
     * @param address address string
     * @param data    short data
     */
    public void writeInt16(String address, Short... data) {
        this.writeInt16(address, Arrays.asList(data));
    }

    /**
     * Write int16.
     * (写入多个Int16数据)
     *
     * @param address address string
     * @param data    short data list
     */
    public void writeInt16(String address, List<Short> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("列表为空");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 2, true);
        data.forEach(buff::putShort);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write uint16.
     * (写入1个UInt16数据)
     *
     * @param address address string
     * @param data    int data
     */
    public void writeUInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write uint16.
     * (写入多个UInt16数据)
     *
     * @param address address string
     * @param data    int data
     */
    public void writeUInt16(String address, Integer... data) {
        this.writeUInt16(address, Arrays.asList(data));
    }

    /**
     * Write uint16.
     * (写入多个UInt16数据)
     *
     * @param address address string
     * @param data    int data list
     */
    public void writeUInt16(String address, List<Integer> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("列表为空");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 2, true);
        data.forEach(buff::putShort);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write int32.
     * (写入1个Int32数据)
     *
     * @param address address string
     * @param data    int data
     */
    public void writeInt32(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write int32.
     * (写入多个Int32数据)
     *
     * @param address address string
     * @param data    int data
     */
    public void writeInt32(String address, Integer... data) {
        this.writeInt32(address, Arrays.asList(data));
    }

    /**
     * Write int32.
     * (写入多个Int32数据)
     *
     * @param address address string
     * @param data    int data
     */
    public void writeInt32(String address, List<Integer> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("列表为空");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 4, EByteBuffFormat.AB_CD);
        data.forEach(buff::putInteger);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write uint32.
     * (写入1个UInt32数据)
     *
     * @param address address string
     * @param data    long data
     */
    public void writeUInt32(String address, long data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putInteger(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write uint32.
     * (写入多个UInt32数据)
     *
     * @param address address string
     * @param data    long data
     */
    public void writeUInt32(String address, Long... data) {
        this.writeUInt32(address, Arrays.asList(data));
    }

    /**
     * Write uint32.
     * (写入多个UInt32数据)
     *
     * @param address address string
     * @param data    long data list
     */
    public void writeUInt32(String address, List<Long> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("data list is empty");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 4, EByteBuffFormat.AB_CD);
        data.forEach(buff::putInteger);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write float32.
     * (写入1个Float32数据)
     *
     * @param address address string
     * @param data    float data
     */
    public void writeFloat32(String address, float data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, EByteBuffFormat.AB_CD).putFloat(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write float32.
     * (写入多个Float32数据)
     *
     * @param address address string
     * @param data    float data
     */
    public void writeFloat32(String address, Float... data) {
        this.writeFloat32(address, Arrays.asList(data));
    }

    /**
     * Write float32.
     * (写入多个Float32数据)
     *
     * @param address address string
     * @param data    float data list
     */
    public void writeFloat32(String address, List<Float> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("data list is empty");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 4, EByteBuffFormat.AB_CD);
        data.forEach(buff::putFloat);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write float64.
     * (写入1个Float64数据)
     *
     * @param address address string
     * @param data    double data
     */
    public void writeFloat64(String address, double data) {
        byte[] bytes = ByteWriteBuff.newInstance(8, EByteBuffFormat.AB_CD).putDouble(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * Write float64.
     * (写入多个Float64数据)
     *
     * @param address address string
     * @param data    double data
     */
    public void writeFloat64(String address, Double... data) {
        this.writeFloat64(address, Arrays.asList(data));
    }

    /**
     * Write float64.
     * (写入多个Float64数据)
     *
     * @param address address string
     * @param data    double list
     */
    public void writeFloat64(String address, List<Double> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("data list is empty");
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(data.size() * 8, EByteBuffFormat.AB_CD);
        data.forEach(buff::putDouble);
        this.writeBytes(address, buff.getData());
    }

    /**
     * Write string.
     * (写入字符串数据)
     *
     * @param address address string
     * @param data    string data
     */
    public void writeString(String address, String data) {
        byte[] bytes = ByteWriteBuff.newInstance(data.length(), true).putString(data).getData();
        this.writeBytes(address, bytes);
    }

    //endregion
}
