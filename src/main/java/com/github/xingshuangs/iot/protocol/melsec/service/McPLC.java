package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;

import java.util.List;

/**
 * 三菱的PLC
 *
 * @author xingshuang
 */
public class McPLC extends McNetwork {

    public List<Boolean> readBoolean(String address, int count) {
        if (count < 1 || count > 7168) {
            throw new McCommException("count < 1 || count > 7168");
        }
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(this.series, address, count);
        byte[] bytes = this.readDeviceBatchInBit(deviceAddress);
        List<Boolean> res = this.getBooleanList(bytes);
        return res.subList(0, count);
    }

    public byte readByte(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return bytes[0];
    }

    public byte[] readBytes(String address, int count) {
        if (count < 1 || count > 960) {
            throw new McCommException("count < 1 || count > 960");
        }
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(this.series, address, count);
        return this.readDeviceBatchInWord(deviceAddress);
    }
}
