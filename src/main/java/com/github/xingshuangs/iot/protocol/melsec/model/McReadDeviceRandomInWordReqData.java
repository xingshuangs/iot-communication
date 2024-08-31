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


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机读请求数据，字单位
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McReadDeviceRandomInWordReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceAddress> wordAddresses;

    /**
     * 软元件设备地址，双字访问地址列表
     */
    private List<McDeviceAddress> dwordAddresses;

    public McReadDeviceRandomInWordReqData() {
        this(EMcSeries.Q_L, new ArrayList<>(), new ArrayList<>());
    }

    public McReadDeviceRandomInWordReqData(EMcSeries series) {
        this(series, new ArrayList<>(), new ArrayList<>());
    }

    public McReadDeviceRandomInWordReqData(EMcSeries series,
                                           List<McDeviceAddress> wordAddresses,
                                           List<McDeviceAddress> dwordAddresses) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            throw new McCommException("Frame 1E not supported");
        }
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_RANDOM_READ_IN_UNITS;
        this.subcommand = series != EMcSeries.IQ_R ? 0x0000 : 0x0002;
        this.wordAddresses = wordAddresses;
        this.dwordAddresses = dwordAddresses;
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordAddresses.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum()
                + this.dwordAddresses.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordAddresses.size())
                .putByte(this.dwordAddresses.size());
        this.wordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        this.dwordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        return buff.getData();
    }
}
