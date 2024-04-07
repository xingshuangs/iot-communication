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


import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

import java.util.regex.Matcher;
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
        return series.getDeviceCodeByteLength() + series.getHeadDeviceNumberByteLength();
    }

    /**
     * 包含软元件点数的字节数组长度
     *
     * @param series PLC的类型系列
     * @return 字节数组长度
     */
    public int byteArrayLengthWithPointsCount(EMcSeries series) {
        return 2 + series.getDeviceCodeByteLength() + series.getHeadDeviceNumberByteLength();
    }

    /**
     * 不包含软元件点数的字节内容
     *
     * @param series PLC的类型系列
     * @return 字节数组
     */
    public byte[] toByteArrayWithoutPointsCount(EMcSeries series) {
        int length = series.getDeviceCodeByteLength() + series.getHeadDeviceNumberByteLength();
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
        int length = 2 + series.getDeviceCodeByteLength() + series.getHeadDeviceNumberByteLength();
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
            throw new IllegalArgumentException("address is null or empty");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("count <= 0");
        }
        // 转换为大写
        address = address.toUpperCase();
        Matcher matcher = Pattern.compile("\\d").matcher(address);
        if (!matcher.find()) {
            // 地址有问题
            throw new McCommException("address error");
        }

        // 提取字符数据
//        String letter = Pattern.compile("\\d").matcher(address).replaceAll("").trim().toUpperCase();
        String letter = address.substring(0, matcher.start()).trim();
        EMcDeviceCode deviceCode = EMcDeviceCode.from(letter);
        if (deviceCode == null) {
            throw new McCommException("device code is not exist");
        }
        // 提取数字数据
//        String number = Pattern.compile("\\D").matcher(address).replaceAll("").trim();
        String number = address.substring(matcher.start()).trim();
        if ("".equals(number)) {
            throw new McCommException("address of device is error");
        }
        int headDeviceNumber = Integer.parseInt(number, deviceCode.getNotation());
        return new McDeviceAddress(deviceCode, headDeviceNumber, count);
    }
}
