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

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

/**
 * 软元件设备地址+内容
 *
 * @author xingshuang
 */
@Data
public class McDeviceContent extends McDeviceAddress {

    /**
     * 数据内容
     */
    private byte[] data;

    public McDeviceContent() {
    }

    public McDeviceContent(byte[] data) {
        this.data = data;
    }

    public McDeviceContent(EMcDeviceCode deviceCode, int headDeviceNumber, byte[] data) {
        this(deviceCode, headDeviceNumber, 1, data);
    }

    public McDeviceContent(EMcDeviceCode deviceCode, int headDeviceNumber, int devicePointsCount, byte[] data) {
        super(deviceCode, headDeviceNumber, devicePointsCount);
        this.data = data;
    }

    @Override
    public int byteArrayLengthWithoutPointsCount(EMcSeries series) {
        return this.data.length + super.byteArrayLengthWithoutPointsCount(series);
    }

    @Override
    public int byteArrayLengthWithPointsCount(EMcSeries series) {
        return this.data.length + super.byteArrayLengthWithPointsCount(series);
    }

    @Override
    public byte[] toByteArrayWithoutPointsCount(EMcSeries series) {
        int length = this.data.length + super.byteArrayLengthWithoutPointsCount(series);
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        buff.putBytes(super.toByteArrayWithoutPointsCount(series));
        buff.putBytes(this.data);
        return buff.getData();
    }

    @Override
    public byte[] toByteArrayWithPointsCount(EMcSeries series) {
        int length = this.data.length + super.byteArrayLengthWithPointsCount(series);
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        buff.putBytes(super.toByteArrayWithPointsCount(series));
        buff.putBytes(this.data);
        return buff.getData();
    }

    public static McDeviceContent createByAddress(McDeviceAddress deviceAddress, byte[] data) {
        McDeviceContent deviceContent = new McDeviceContent();
        deviceContent.headDeviceNumber = deviceAddress.headDeviceNumber;
        deviceContent.deviceCode = deviceAddress.deviceCode;
        deviceContent.devicePointsCount = deviceAddress.devicePointsCount;
        deviceContent.data = data;
        return deviceContent;
    }

    public static McDeviceContent createBy(String address, byte[] data) {
        return createBy(address, 1, data);
    }

    public static McDeviceContent createBy(String address, int count, byte[] data) {
        McDeviceAddress deviceAddress = McDeviceAddress.createBy(address, count);
        return createByAddress(deviceAddress, data);
    }
}
