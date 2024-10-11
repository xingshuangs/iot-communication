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

package com.github.xingshuangs.iot.protocol.s7.algorithm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Group item.
 * (合并项)
 *
 * @author xingshuang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class S7ComItem {

    /**
     * Data index.
     * 数据索引
     */
    private int index;

    /**
     * Raw data size.
     * (原始数据大小)
     */
    private int rawSize;

    /**
     * Split index offset.
     * (分割点，即数据偏移索引)
     */
    private int splitOffset;

    /**
     * Ripe data size after split.
     * (分割后的数据大小)
     */
    private int ripeSize;

    /**
     * Extra data size.
     * (额外需要的数据大小)
     */
    private int extraSize;

    /**
     * Threshold.
     * (阀值)
     */
    private int threshold = 0;

    /**
     * Get total length of item.
     * (整个长度)
     *
     * @return total length
     */
    public int getTotalLength() {
        return Math.max(this.ripeSize + this.extraSize, this.threshold);
    }
}
