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

package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 多地址读
 *
 * @author xingshuang
 */
@Data
public class McMultiAddressRead {

    /**
     * word列表
     */
    private final List<McDeviceAddress> words = new ArrayList<>();

    /**
     * dword列表
     */
    private final List<McDeviceAddress> dwords = new ArrayList<>();

    /**
     * 添加获取word的地址
     *
     * @param address 地址
     * @return McMultiAddressRead
     */
    public McMultiAddressRead addWordData(String address) {
        this.words.add(McDeviceAddress.createBy(address));
        return this;
    }

    /**
     * 添加获取word的地址
     *
     * @param address 地址
     * @return McMultiAddressRead
     */
    public McMultiAddressRead addDWordData(String address) {
        this.dwords.add(McDeviceAddress.createBy(address));
        return this;
    }
}
