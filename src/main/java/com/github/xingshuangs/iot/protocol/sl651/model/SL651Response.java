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
package com.github.xingshuangs.iot.protocol.sl651.model;

import lombok.Data;

import java.io.Serializable;

/**
 * SL651响应报文头
 *
 * @author wangqingpan
 */
@Data
public class SL651Response implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 中心站地址
     */
    private byte[] centerStation = new byte[1];

    /**
     * 遥测站地址
     */
    private byte[] remoteStationAddress = new byte[5];

    /**
     * 密码
     */
    private byte[] password = new byte[2];

    /**
     * 功能码
     */
    private byte[] functionCode = new byte[1];

    /**
     * 数据长度(第一个0表示上行，8表示下行，剩下3位表示16进制的数据长度)
     */
    private byte[] dataLength = new byte[2];

    /**
     * 流水号
     */
    private byte[] serialNumber = new byte[2];

    /**
     * 时间
     */
    private byte[] timestamp = new byte[6];

    /**
     * 测站地址
     */
    private byte[] stationAddress = new byte[5];

    /**
     * 遥测站类型
     */
    private byte[] stationType = new byte[1];

}
