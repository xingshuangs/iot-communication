package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;

import java.util.List;

/**
 * 三菱的PLC
 *
 * @author xingshuang
 */
public class McPLC extends McNetwork {


    public boolean readBoolean(String address) {
        List<Boolean> booleans = this.readBooleans(address, 1);
        return booleans.get(0);
    }

    public List<Boolean> readBooleans(String address, int count) {
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInBit(deviceAddress);
        List<Boolean> res = this.getBooleanList(deviceContent.getData());
        return res.subList(0, count);
    }

    public byte readByte(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return bytes[0];
    }

    public byte[] readBytes(String address, int count) {
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInWord(deviceAddress);
        return deviceContent.getData();
    }

    public short readInt16(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return ByteReadBuff.newInstance(bytes, true).getInt16();
    }

    public int readUInt16(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return ByteReadBuff.newInstance(bytes, true).getUInt16();
    }

    public int readInt32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getInt32();
    }

    public long readUInt32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getUInt32();
    }

    public float readFloat32(String address) {
        byte[] bytes = this.readBytes(address, 2);
        return ByteReadBuff.newInstance(bytes, true).getFloat32();
    }

    public double readFloat64(String address) {
        byte[] bytes = this.readBytes(address, 4);
        return ByteReadBuff.newInstance(bytes, true).getFloat64();
    }




}
