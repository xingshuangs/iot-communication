package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

/**
 * 软元件设备地址
 *
 * @author xingshuang
 */
@Data
public class McDeviceAddress {

    /**
     * PLC的类型系列
     */
    protected EMcSeries series = EMcSeries.Q_L;

    /**
     * 起始软元件编号，Q/L系列是3个字节，iQ-R系列是4个字节
     */
    protected int headDeviceNumber = 0;

    /**
     * 软元件代码，Q/L系列是1个字节，iQ-R系列是2个字节
     */
    protected EMcDeviceCode deviceCode = EMcDeviceCode.D;

    /**
     * 软元件点数，2个字节，注意：该字段不统计在字节计算中，给外部统计使用
     */
    protected int devicePointsCount = 0;

    public McDeviceAddress() {
    }

    public McDeviceAddress(int headDeviceNumber, EMcDeviceCode deviceCode, int devicePointsCount) {
        this.headDeviceNumber = headDeviceNumber;
        this.deviceCode = deviceCode;
        this.devicePointsCount = devicePointsCount;
    }

    public McDeviceAddress(EMcSeries series, int headDeviceNumber, EMcDeviceCode deviceCode, int devicePointsCount) {
        this.series = series;
        this.headDeviceNumber = headDeviceNumber;
        this.deviceCode = deviceCode;
        this.devicePointsCount = devicePointsCount;
    }

    /**
     * 不包含软元件点数的字节数组长度
     *
     * @return 字节数组长度
     */
    public int byteArrayLengthWithoutPointsCount() {
        return this.series == EMcSeries.Q_L ? 4 : 6;
    }

    /**
     * 包含软元件点数的字节数组长度
     *
     * @return 字节数组长度
     */
    public int byteArrayLengthWithPointsCount() {
        return 2 + (this.series == EMcSeries.Q_L ? 4 : 6);
    }

    /**
     * 不包含软元件点数的字节内容
     *
     * @return 字节数组
     */
    public byte[] toByteArrayWithoutPointsCount() {
        int length = this.series == EMcSeries.Q_L ? 4 : 6;
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        if (this.series == EMcSeries.Q_L) {
            buff.putBytes(IntegerUtil.toCustomByteArray(this.headDeviceNumber, 0, 3, true));
            buff.putByte(this.deviceCode.getBinaryCode());
        } else {
            buff.putInteger(this.headDeviceNumber);
            buff.putShort(this.deviceCode.getBinaryCodeIqr());
        }
        return buff.getData();
    }

    /**
     * 包含软元件点数的字节内容
     *
     * @return 字节数组
     */
    public byte[] toByteArrayWithPointsCount() {
        int length = 2 + (this.series == EMcSeries.Q_L ? 4 : 6);
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        if (this.series == EMcSeries.Q_L) {
            buff.putBytes(IntegerUtil.toCustomByteArray(this.headDeviceNumber, 0, 3, true));
            buff.putByte(this.deviceCode.getBinaryCode());
        } else {
            buff.putInteger(this.headDeviceNumber);
            buff.putShort(this.deviceCode.getBinaryCodeIqr());
        }
        buff.putShort(this.devicePointsCount);
        return buff.getData();
    }
}
