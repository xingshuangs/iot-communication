package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * 软元件设备地址
 *
 * @author xingshuang
 */
@Data
public class McDeviceAddress {

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
    protected int devicePointsCount = 1;

    public McDeviceAddress() {
    }

    public McDeviceAddress(EMcDeviceCode deviceCode, int headDeviceNumber) {
        this(deviceCode, headDeviceNumber, 1);
    }

    public McDeviceAddress(EMcDeviceCode deviceCode, int headDeviceNumber, int devicePointsCount) {
        this.headDeviceNumber = headDeviceNumber;
        this.deviceCode = deviceCode;
        this.devicePointsCount = devicePointsCount;
    }

    /**
     * 不包含软元件点数的字节数组长度
     *
     * @param series PLC的类型系列
     * @return 字节数组长度
     */
    public int byteArrayLengthWithoutPointsCount(EMcSeries series) {
        return series == EMcSeries.Q_L ? 4 : 6;
    }

    /**
     * 包含软元件点数的字节数组长度
     *
     * @param series PLC的类型系列
     * @return 字节数组长度
     */
    public int byteArrayLengthWithPointsCount(EMcSeries series) {
        return 2 + (series == EMcSeries.Q_L ? 4 : 6);
    }

    /**
     * 不包含软元件点数的字节内容
     *
     * @param series PLC的类型系列
     * @return 字节数组
     */
    public byte[] toByteArrayWithoutPointsCount(EMcSeries series) {
        int length = series == EMcSeries.Q_L ? 4 : 6;
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        if (series == EMcSeries.Q_L) {
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
     * @param series PLC的类型系列
     * @return 字节数组
     */
    public byte[] toByteArrayWithPointsCount(EMcSeries series) {
        int length = 2 + (series == EMcSeries.Q_L ? 4 : 6);
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length, true);
        if (series == EMcSeries.Q_L) {
            buff.putBytes(IntegerUtil.toCustomByteArray(this.headDeviceNumber, 0, 3, true));
            buff.putByte(this.deviceCode.getBinaryCode());
        } else {
            buff.putInteger(this.headDeviceNumber);
            buff.putShort(this.deviceCode.getBinaryCodeIqr());
        }
        buff.putShort(this.devicePointsCount);
        return buff.getData();
    }

    /**
     * 构建McDeviceAddress
     *
     * @param address 地址
     * @return McDeviceAddress对象
     */
    public static McDeviceAddress createBy(String address) {
        return createBy(address, 1);
    }

    /**
     * 构建McDeviceAddress
     *
     * @param address 地址
     * @param count   个数
     * @return McDeviceAddress对象
     */
    public static McDeviceAddress createBy(String address, int count) {
        if (address == null || address.length() == 0) {
            throw new IllegalArgumentException("address不能为空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("count个数必须为正数");
        }
        // 提取字符数据
        String letter = Pattern.compile("\\d").matcher(address).replaceAll("").trim().toUpperCase();
        EMcDeviceCode deviceCode = EMcDeviceCode.from(letter);
        // 提取数字数据
        String number = Pattern.compile("\\D").matcher(address).replaceAll("").trim();
        int headDeviceNumber = Integer.parseInt(number);
        return new McDeviceAddress(deviceCode, headDeviceNumber, count);
    }
}
