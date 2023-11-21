package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;

import java.util.List;

/**
 * 三菱的PLC
 *
 * @author xingshuang
 */
public class McPLC extends McNetwork {

    public List<Boolean> readBoolean(String address, int count) {

        McDeviceAddress deviceAddress = McDeviceAddress.createBy(this.series, address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInBit(deviceAddress);
        List<Boolean> res = this.getBooleanList(deviceContent.getData());
        return res.subList(0, count);
    }

    public byte readByte(String address) {
        byte[] bytes = this.readBytes(address, 1);
        return bytes[0];
    }

    public byte[] readBytes(String address, int count) {

        McDeviceAddress deviceAddress = McDeviceAddress.createBy(this.series, address, count);
        McDeviceContent deviceContent = this.readDeviceBatchInWord(deviceAddress);
        return deviceContent.getData();
    }
}
