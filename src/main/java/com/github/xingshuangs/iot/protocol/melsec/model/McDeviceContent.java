package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
