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


import lombok.Data;

/**
 * Related properties of the sample,
 * DependsOn=1, IsNonSync =1, ISnonsync = 0, DependsOn=1, ISnonsync = 0, as long as keyframe is touched, DependsOn=2, others = 0.
 * 采样的相关属性
 * 这里以单个分片单个sample的模式，只要碰到关键帧，DependsOn=2 ，其他等于0，非关键帧DependsOn=1，IsNonSync等于1，其他等于0,
 *
 * @author xingshuang
 */
@Data
public class Mp4SampleFlag {

    private int isLeading = 0;

    private int isDependedOn = 0;

    private int hasRedundancy = 0;

    private int degradPrio = 0;

    private int dependedOn = 0;

    private int isNonSync = 0;

    private int paddingValue = 0;
}
