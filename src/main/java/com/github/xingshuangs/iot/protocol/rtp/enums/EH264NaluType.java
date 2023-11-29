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

package com.github.xingshuangs.iot.protocol.rtp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * H264的Nalu类别
 *
 * @author xingshuang
 */
public enum EH264NaluType {

    /**
     * 未指定
     */
    NONE(0),

    /**
     * 非 IDR 图像的片
     */
    NON_IDR_SLICE(1),

    /**
     * 片数据A分区
     */
    DPA(2),

    /**
     * 片数据B分区
     */
    DPB(3),

    /**
     * 片数据C分区
     */
    DPC(4),

    /**
     * IDR 图像的片
     */
    IDR_SLICE(5),

    /**
     * SEI（辅助增强信息）
     */
    SEI(6),

    /**
     * SPS（序列参数集）
     */
    SPS(7),

    /**
     * PPS（图像参数集）
     */
    PPS(8),

    /**
     * 分界符
     */
    AUD(9),

    /**
     * 序列结束
     */
    END_OF_SEQUENCE(10),

    /**
     * 码流结束
     */
    END_OF_STREAM(11),

    /**
     * 填充
     */
    FILLER_DATA(12),

    /**
     * STAP-A（单一时间组合包模式 A，用于一个 RTP 包荷载多个 NALU）
     */
    STAP_A(24),

    /**
     * STAP-B（单一时间组合包模式 B）
     */
    STAP_B(25),

    /**
     * MTAP16（多个时间的组合包模式 A）
     */
    MTAP16(26),

    /**
     * MTAP24（多个时间的组合包模式 B）
     */
    MTAP24(27),

    /**
     * FU-A（分片模式 A，用于将单个 NALU 分到多个 RTP 包）
     */
    FU_A(28),

    /**
     * FU-B（分片模式 B）
     */
    FU_B(29),

    ;

    private static Map<Integer, EH264NaluType> map;

    public static EH264NaluType from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (EH264NaluType item : EH264NaluType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    EH264NaluType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
