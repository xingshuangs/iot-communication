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

package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * Mp4 sample data.
 *
 * @author xingshuang
 */
@Data
public class Mp4SampleData {

    /**
     * DTS
     */
    private long dts = 0;

    /**
     * nalu length + nalu data.
     * （数据，帧数据+长度）
     */
    private byte[] data;

    /**
     * Data size.
     * 数据大小，表示sample对应的数据帧的实际大小size=224251，帧数据字节+长度字节
     */
    private int size = 0;

    /**
     * Duration.
     * 持续时间，一个sample的持续时间duration=3600
     */
    private int duration = 3600;

    /**
     * CTS = PTS - DTS
     * 时间, cts一般取值为0
     */
    private int cts = 0;

    /**
     * Identifier bit information, representing sample-flags, DependsOn=2 and other = 0 if a keyframe is touched,
     * DependsOn=1 for non-keyframes, IsNonSync =1 and other = 0.
     * 标识位信息，表示sample-flags，只要碰到关键帧，DependsOn=2 ，其他等于0，非关键帧DependsOn=1，IsNonSync等于1，其他等于0
     * (sample.IsLeading << 2) | sample.DependsOn,
     * (sample.IsDependedOn << 6) | (sample.HasRedundancy << 4) | (0x00 << 1) | sample.IsNonSync,
     * sample.DegradPrio & 0xF0 << 8,
     * sample.DegradPrio & 0x0F,
     */
    private Mp4SampleFlag flags = new Mp4SampleFlag();

    public void setData(byte[] data) {
        this.size = 4 + data.length;
        ByteWriteBuff buff = new ByteWriteBuff(4 + data.length);
        buff.putInteger(data.length)
                .putBytes(data);
        this.data = buff.getData();
    }
}
