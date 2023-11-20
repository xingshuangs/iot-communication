package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;

import java.util.regex.Pattern;

/**
 * MC协议地址解析工具
 * DB1.0.1、DB1.1、DB100.DBX0.0、DB100.DBB5、DB100.DBW6
 * M1.1、M1、MB1、MW1、MD1
 * V1.1、V1、VB100、VW100、VD100
 * I0.1、I0、IB1、IW1、ID1
 * Q0.1、Q0、QB1、QW1、QD1
 *
 * @author xingshuang
 */
public class McAddressUtil {

    private McAddressUtil() {
        // NOOP
    }

    /**
     * 解析请求内容
     *
     * @param address 地址
     * @param count   个数
     * @return McDeviceAddress
     */
    public static McDeviceAddress parse(String address, int count) {
        if (address == null || address.length() == 0) {
            throw new IllegalArgumentException("address不能为空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("count个数必须为正数");
        }
        String letter = Pattern.compile("\\d").matcher(address).replaceAll("").trim().toUpperCase();
        EMcDeviceCode deviceCode = EMcDeviceCode.from(letter);
        String number = Pattern.compile("\\D").matcher(address).replaceAll("").trim();
        int headDeviceNumber = Integer.parseInt(number);
        return new McDeviceAddress(deviceCode, headDeviceNumber, count);
    }
}
