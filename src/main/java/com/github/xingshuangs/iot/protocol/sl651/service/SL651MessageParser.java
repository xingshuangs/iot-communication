/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>, 王清攀
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
package com.github.xingshuangs.iot.protocol.sl651.service;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.protocol.sl651.enums.SL651FunctionCodeEnum;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651BodyResponse;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651Response;
import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SL651报文解析器
 *
 * @author wangqingpan
 */
public class SL651MessageParser {

    /**
     * 从字节数组解析SL651响应参数
     *
     * @param bytes 字节数组
     * @return SL651 响应参数
     */
    public static SL651Response parseRespParams(byte[] bytes) {
        SL651Response params = new SL651Response();

        // 中心站地址
        byte[] centerStationBytes = new byte[1];
        System.arraycopy(bytes, 2, centerStationBytes, 0, 1);

        // 遥测站地址
        byte[] remoteStationAddressBytes = new byte[5];
        System.arraycopy(bytes, 3, remoteStationAddressBytes, 0, 5);

        // 密码
        byte[] passwordBytes = new byte[2];
        System.arraycopy(bytes, 8, passwordBytes, 0, 2);

        // 功能码
        byte[] functionCodeBytes = new byte[1];
        System.arraycopy(bytes, 10, functionCodeBytes, 0, 1);

        // 数据长度
        byte[] dataLengthBytes = new byte[2];
        System.arraycopy(bytes, 11, dataLengthBytes, 0, 2);

        // 流水号
        byte[] serialNumberBytes = new byte[2];
        System.arraycopy(bytes, 14, serialNumberBytes, 0, 2);

        // 时间
        byte[] timestampBytes = new byte[6];
        System.arraycopy(bytes, 16, timestampBytes, 0, 6);

        // 如果不是心跳报文执行
        if (!Arrays.equals(SL651FunctionCodeEnum.HEART_BEAT.getCode(), functionCodeBytes)) {
            // 测站地址
            byte[] stationAddressBytes = new byte[5];
            System.arraycopy(bytes, 24, stationAddressBytes, 0, 5);

            // 遥测站类型
            byte[] stationTypeBytes = new byte[1];
            System.arraycopy(bytes, 29, stationTypeBytes, 0, 1);

            params.setStationAddress(stationAddressBytes);
            params.setStationType(stationTypeBytes);
        }

        params.setCenterStation(centerStationBytes);
        params.setRemoteStationAddress(remoteStationAddressBytes);
        params.setPassword(passwordBytes);
        params.setFunctionCode(functionCodeBytes);
        params.setDataLength(dataLengthBytes);
        params.setSerialNumber(serialNumberBytes);
        params.setTimestamp(timestampBytes);

        return params;
    }

    /**
     * 从字节数组解析SL651响应报文体参数
     *
     * @param bytes 字节
     * @return SL651响应体参数列表
     */
    public static List<SL651BodyResponse> parseRespBodyParams(byte[] bytes) {
        List<SL651BodyResponse> resultList = new ArrayList<>();

        // 获取数据部分进行解析
        // 数据部分=全部字节-遥测站类型之前的字节-结束符和CRC校验
        int bodyLength = bytes.length - 30 - 3;
        byte[] dataBytes = new byte[bodyLength];
        System.arraycopy(bytes, 30, dataBytes, 0, bodyLength);

        // 使用索引定位处理的字节位数
        int index = 0;

        // 获取观测时间
        // 观测时间标识符为0xF0 0xF0
        // 此处省略了标识符，只获取观测时间有效字节
        byte[] observeTimeBytes = new byte[5];

        while (index < dataBytes.length) {
            int startIndex = index;

            // 处理0xFF（无效字节）标识符的情况
            // 如果第一个字节是0xFF，则使用下一个字节作为标识符
            if ((dataBytes[index] & 0xFF) == 0xFF) {
                index++;

                // 处理 F0 标识符（观测时间标识符）
                // 其中2代表标识符的两个字节，5代表观测时间有效字节的长度
            } else if (Arrays.equals(
                    new byte[]{dataBytes[index], dataBytes[index + 1]},
                    new byte[]{(byte) 0xF0, (byte) 0xF0}
            )) {
                if (index + 2 + 5 <= dataBytes.length) {
                    System.arraycopy(dataBytes, index + 2, observeTimeBytes, 0, 5);
                    index += 7;
                } else {
                    index = dataBytes.length;
                }

                // 处理 F4 标识符（雨量数据）
                // 其中2包括了标识符的一个字节，数据长度一个字节，12代表雨量数据占的总字节
            } else if ((dataBytes[index] & 0xFF) == ((byte) 0xF4 & 0xFF)) {
                SL651BodyResponse bodyParams = createBodyParams(observeTimeBytes, dataBytes[index]);
                int length = getLength(dataBytes[index + 1]);

                if (index + 2 + length <= dataBytes.length) {
                    byte[] newDataBytes = new byte[length];
                    System.arraycopy(dataBytes, index + 2, newDataBytes, 0, length);

                    List<String> bodyElements = new ArrayList<>();
                    for (byte newDataByte : newDataBytes) {
                        bodyElements.add(calRainfallCalculation(new byte[]{newDataByte}).toString());
                    }
                    bodyParams.setBodyElements(bodyElements);
                    resultList.add(bodyParams);
                    index += 2 + length;
                } else {
                    index = dataBytes.length;
                }

                // 处理 F5-F9, FA-FC 标识符（水位数据）
                // 其中2包括了标识符的一个字节，数据长度一个字节，length代表水位数据占的总字节
            } else if (isWaterLevelIdentifier(dataBytes[index])) {
                SL651BodyResponse bodyParams = createBodyParams(observeTimeBytes, dataBytes[index]);
                int length = getLength(dataBytes[index + 1]);

                if (index + 2 + length <= dataBytes.length) {
                    byte[] newDataBytes = new byte[length];
                    System.arraycopy(dataBytes, index + 2, newDataBytes, 0, length);

                    List<String> bodyElements = new ArrayList<>();
                    for (int i = 0; i < newDataBytes.length; i += 2) {
                        byte[] twoBytes = new byte[]{newDataBytes[i], newDataBytes[i + 1]};
                        Double result = calWaterLevelCalculation(twoBytes);
                        bodyElements.add(result.toString());
                    }
                    bodyParams.setBodyElements(bodyElements);
                    resultList.add(bodyParams);
                    index += 2 + length;
                } else {
                    index = dataBytes.length;
                }

                // 处理 89 设备序列号
            } else if ((dataBytes[index] & 0xFF) == ((byte) 0x89 & 0xFF)) {
                SL651BodyResponse bodyParams = createBodyParams(observeTimeBytes, dataBytes[index]);
                int length = getLength(dataBytes[index + 1]);

                if (index + 2 + length <= dataBytes.length) {
                    byte[] newDataBytes = new byte[length];
                    System.arraycopy(dataBytes, index + 2, newDataBytes, 0, length);
                    bodyParams.setBodyElements(Arrays.asList(HexUtil.toHexString(newDataBytes, "")));
                    resultList.add(bodyParams);
                    index += 2 + length;
                } else {
                    index = dataBytes.length;
                }

                // 处理 45 测站类型标识符 遥测站状态及报警信息
                // 固定4个长度
            } else if ((dataBytes[index] & 0xFF) == ((byte) 0x45 & 0xFF)) {
                SL651BodyResponse bodyParams = createBodyParams(observeTimeBytes, dataBytes[index]);

                if (index + 2 + 4 <= dataBytes.length) {
                    byte[] newDataBytes = new byte[4];
                    System.arraycopy(dataBytes, index + 2, newDataBytes, 0, 4);
                    bodyParams.setBodyElements(Arrays.asList(HexUtil.toHexString(newDataBytes, "")));
                    resultList.add(bodyParams);
                    index += 2 + 4;
                } else {
                    index = dataBytes.length;
                }

                // 处理其他标识符（通用数据）
            } else {
                SL651BodyResponse bodyParams = createBodyParams(observeTimeBytes, dataBytes[index]);

                int dataSize = getLength(dataBytes[index + 1]);
                int decimalSize = getDecimalPlaces(dataBytes[index + 1]);

                if (index + 2 + dataSize <= dataBytes.length) {
                    byte[] newDataBytes = new byte[dataSize];
                    System.arraycopy(dataBytes, index + 2, newDataBytes, 0, dataSize);

                    List<String> bodyElements = new ArrayList<>();
                    Double result = calGeneralCalculation(newDataBytes, decimalSize);
                    bodyElements.add(result.toString());
                    bodyParams.setBodyElements(bodyElements);
                    resultList.add(bodyParams);
                    index += 2 + dataSize;
                } else {
                    index = dataBytes.length;
                }
            }

            if (index <= startIndex) {
                throw new SocketRuntimeException("SL651 body parse dead loop detected at index: " + index);
            }
        }
        return resultList;
    }

    /**
     * 创建SL651响应报文体参数
     *
     * @param observeTime    观测时间
     * @param dataIdentifier 数据标识符
     * @return SL651响应体参数
     */
    private static SL651BodyResponse createBodyParams(byte[] observeTime, byte dataIdentifier) {
        SL651BodyResponse bodyParams = new SL651BodyResponse();
        bodyParams.setObserveTime(observeTime);
        bodyParams.setDataIdentifier(new byte[]{dataIdentifier});
        return bodyParams;
    }

    /**
     * 检查是否为水位数据标识符
     *
     * @param identifier 标识符
     * @return 是否为水位标识符
     */
    private static boolean isWaterLevelIdentifier(byte identifier) {
        int value = identifier & 0xFF;
        return value == 0xF5
                || value == 0xF6
                || value == 0xF7
                || value == 0xF8
                || value == 0xF9
                || value == 0xFA
                || value == 0xFB
                || value == 0xFC;
    }

    /**
     * 从头部字节获取数据长度
     *
     * @param header 字节
     * @return 数据长度
     */
    private static int getLength(byte header) {
        return (header >> 3) & 0x1F;
    }

    /**
     * 从头部字节获取小数位数
     *
     * @param header 字节
     * @return 小数点
     */
    private static int getDecimalPlaces(byte header) {
        return header & 0x07;
    }

    /**
     * 计算雨量
     *
     * @param hexArray 字节
     * @return 降雨值
     */
    private static Double calRainfallCalculation(byte[] hexArray) {
        int unsignedByte = hexArray[0] & 0xFF;
        if (unsignedByte == 0xFF) {
            return null;
        } else {
            return (double) unsignedByte / 10;
        }
    }

    /**
     * 计算水位
     *
     * @param waterBytes 字节
     * @return 水位值
     */
    private static Double calWaterLevelCalculation(byte[] waterBytes) {
        int value = ((waterBytes[0] & 0xFF) << 8) | (waterBytes[1] & 0xFF);
        if (value == 0xFFFF) {
            return null;
        }
        return value / 100.0;
    }

    /**
     * 通用计算
     *
     * @param hexArray    字节
     * @param decimalSize 小数点位数
     * @return 计算结果
     */
    private static Double calGeneralCalculation(byte[] hexArray, int decimalSize) {
        if (hexArray == null || hexArray.length == 0) {
            return null;
        }
        long integerPart = 0;
        for (byte b : hexArray) {
            byte processedByte = (b == (byte) 0xFF) ? (byte) 0x00 : b;
            int highNibble = (processedByte >> 4) & 0x0F;
            int lowNibble = processedByte & 0x0F;
            integerPart = integerPart * 100 + highNibble * 10 + lowNibble;
        }
        return integerPart / Math.pow(10, decimalSize);
    }
}