package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.common.constant.GeneralConst;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;

import java.util.Collections;
import java.util.List;

/**
 * 三菱的PLC
 *
 * @author xingshuang
 */
public class McPLC extends McNetwork {

    public McPLC() {
        this(GeneralConst.LOCALHOST, GeneralConst.MELSEC_PORT);
    }

    public McPLC(String host, int port) {
        super(host, port);
        this.tag = "Melsec";
    }

    //region 软元件读取

    /**
     * 读取booleans数据
     *
     * @param address 地址
     * @param count   boolean个数
     * @return boolean列表
     */
    public List<Boolean> readBooleans(String address, int count) {
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInBit(deviceAddress);
        List<Boolean> res = this.getBooleansBy(deviceContent.getData());
        return res.subList(0, count);
    }

    /**
     * 读取字节数组数据
     *
     * @param address 地址
     * @param count   字节个数
     * @return 字节数组
     */
    public byte[] readBytes(String address, int count) {
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInWord(deviceAddress);
        return deviceContent.getData();
    }

    /**
     * 读取1个boolean数据
     *
     * @param address 地址
     * @return boolean
     */
    public boolean readBoolean(String address) {
        List<Boolean> booleans = this.readBooleans(address, 1);
        return booleans.get(0);
    }

    /**
     * 读取1个字节数据
     *
     * @param address 地址
     * @return 字节
     */
    public byte readByte(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return bytes[0];
    }

    /**
     * 读取1个Int16数据
     *
     * @param address 地址
     * @return short数据
     */
    public short readInt16(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return ByteReadBuff.newInstance(bytes, true).getInt16();
    }

    /**
     * 读取1个UInt16数据
     *
     * @param address 地址
     * @return int数据
     */
    public int readUInt16(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return ByteReadBuff.newInstance(bytes, true).getUInt16();
    }

    /**
     * 读取1个Int32数据
     *
     * @param address 地址
     * @return int数据
     */
    public int readInt32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getInt32();
    }

    /**
     * 读取1个UInt32数据
     *
     * @param address 地址
     * @return long数据
     */
    public long readUInt32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getUInt32();
    }

    /**
     * 读取1个Float32数据
     *
     * @param address 地址
     * @return float数据
     */
    public float readFloat32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getFloat32();
    }

    /**
     * 读取1个Float64数据
     *
     * @param address 地址
     * @return double数据
     */
    public double readFloat64(String address) {
        byte[] bytes = this.readBytes(address, 4);
        return ByteReadBuff.newInstance(bytes, true).getFloat64();
    }

    /**
     * 读取字符串数据
     *
     * @param address 地址
     * @param length  字符串长度
     * @return 字符串
     */
    public String readString(String address, int length) {
        int len = length % 2 == 0 ? length : (length + 1);
        byte[] bytes = this.readBytes(address, len / 2);
        return ByteReadBuff.newInstance(bytes, true).getString(length);
    }

    //endregion

    //region 软元件写入

    /**
     * 写入boolean数据列表
     *
     * @param address  地址
     * @param booleans boolean列表
     */
    public void writeBooleans(String address, List<Boolean> booleans) {
        byte[] bytes = this.getBytesBy(booleans);
        McDeviceContent deviceContent = McDeviceContent.createBy(address, bytes.length * 2, bytes);
        this.writeDeviceBatchInBit(deviceContent);
    }

    /**
     * 写入字节数组
     *
     * @param address 地址
     * @param data    字节数组
     */
    public void writeBytes(String address, byte[] data) {
        byte[] newData = data;
        if (data.length % 2 != 0) {
            newData = ByteWriteBuff.newInstance(data.length + 1, true).putBytes(data).getData();
        }
        McDeviceContent deviceContent = McDeviceContent.createBy(address, newData.length / 2, newData);
        this.writeDeviceBatchInWord(deviceContent);
    }

    /**
     * 写入1个boolean数据
     *
     * @param address 地址
     * @param data    boolean数据
     */
    public void writeBoolean(String address, boolean data) {
        this.writeBooleans(address, Collections.singletonList(data));
    }

    /**
     * 写入1个字节数据
     *
     * @param address 地址
     * @param data    字节数据
     */
    public void writeByte(String address, byte data) {
        this.writeBytes(address, new byte[]{data});
    }

    /**
     * 写入1个Int16数据
     *
     * @param address 地址
     * @param data    short数据
     */
    public void writeInt16(String address, short data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入1个UInt16数据
     *
     * @param address 地址
     * @param data    int数据
     */
    public void writeUInt16(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(2, true).putShort(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入1个Int32数据
     *
     * @param address 地址
     * @param data    int数据
     */
    public void writeInt32(String address, int data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, true).putInteger(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入1个UInt32数据
     *
     * @param address 地址
     * @param data    long数据
     */
    public void writeUInt32(String address, long data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, true).putInteger(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入1个Float32数据
     *
     * @param address 地址
     * @param data    float数据
     */
    public void writeFloat32(String address, float data) {
        byte[] bytes = ByteWriteBuff.newInstance(4, true).putFloat(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入1个Float64数据
     *
     * @param address 地址
     * @param data    double数据
     */
    public void writeFloat64(String address, double data) {
        byte[] bytes = ByteWriteBuff.newInstance(8, true).putDouble(data).getData();
        this.writeBytes(address, bytes);
    }

    /**
     * 写入字符串数据
     *
     * @param address 地址
     * @param data    字符串数据
     */
    public void writeString(String address, String data) {
        byte[] bytes = ByteWriteBuff.newInstance(data.length(), true).putString(data).getData();
        this.writeBytes(address, bytes);
    }

    //endregion

}
