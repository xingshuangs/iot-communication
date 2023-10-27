package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLengthWithoutPointsCount(), true);
        buff.putBytes(super.toByteArrayWithoutPointsCount());
        buff.putBytes(this.data);
        return buff.getData();
    }

    @Override
    public byte[] toByteArrayWithPointsCount() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLengthWithPointsCount(), true);
        buff.putBytes(super.toByteArrayWithPointsCount());
        buff.putBytes(this.data);
        return buff.getData();
    }
}
