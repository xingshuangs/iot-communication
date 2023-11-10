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

    public McDeviceContent(int headDeviceNumber, EMcDeviceCode deviceCode, int devicePointsCount, byte[] data) {
        super(headDeviceNumber, deviceCode, devicePointsCount);
        this.data = data;
    }

    public McDeviceContent(EMcSeries series, int headDeviceNumber, EMcDeviceCode deviceCode, int devicePointsCount, byte[] data) {
        super(series, headDeviceNumber, deviceCode, devicePointsCount);
        this.data = data;
    }

    @Override
    public int byteArrayLengthWithoutPointsCount() {
        return this.data.length + super.byteArrayLengthWithoutPointsCount();
    }

    @Override
    public int byteArrayLengthWithPointsCount() {
        return this.data.length + super.byteArrayLengthWithPointsCount();
    }

    @Override
    public byte[] toByteArrayWithoutPointsCount() {
        int length = this.data.length + super.byteArrayLengthWithoutPointsCount();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        buff.putBytes(super.toByteArrayWithoutPointsCount());
        buff.putBytes(this.data);
        return buff.getData();
    }

    @Override
    public byte[] toByteArrayWithPointsCount() {
        int length = this.data.length + super.byteArrayLengthWithPointsCount();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        buff.putBytes(super.toByteArrayWithPointsCount());
        buff.putBytes(this.data);
        return buff.getData();
    }
}
