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
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机写请求数据，位单位
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceRandomInBitReqData extends McReqData {

    /**
     * 软元件设备内容，位访问地址列表
     */
    private List<McDeviceContent> bitContents;

    public McWriteDeviceRandomInBitReqData() {
        this(EMcSeries.Q_L, new ArrayList<>());
    }

    public McWriteDeviceRandomInBitReqData(EMcSeries series) {
        this(series, new ArrayList<>());
    }

    public McWriteDeviceRandomInBitReqData(EMcSeries series, List<McDeviceContent> bitContents) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS;
        this.subcommand = series != EMcSeries.IQ_R ? 0x0001 : 0x0003;
        this.bitContents = bitContents;
    }

    @Override
    public int byteArrayLength() {
        return (this.series.getFrameType() == EMcFrameType.FRAME_1E ? 2 : 5)
                + this.bitContents.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true);
        if (this.series.getFrameType() == EMcFrameType.FRAME_1E) {
            buff.putShort(this.bitContents.size());
        } else {
            buff.putShort(this.command.getCode())
                    .putShort(this.subcommand)
                    .putByte(this.bitContents.size());
        }
        this.bitContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        return buff.getData();
    }
}
