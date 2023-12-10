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

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckArea;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckModule;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestNckItem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author xingshuang
 */
public class DemoS7Nck1 {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "127.0.0.1");

        // single request
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 4, ENckModule.M, 1);
        DataItem dataItem = s7PLC.readS7NckData(requestNckItem);
        String cncType = ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
        System.out.println(cncType);

        // multi request
        List<RequestNckItem> requestNckItems = IntStream.of(1, 2, 3, 4)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, x, ENckModule.SMA, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = s7PLC.readS7NckData(requestNckItems);
        List<Double> positions = dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
        positions.forEach(System.out::println);

        s7PLC.close();
    }
}
