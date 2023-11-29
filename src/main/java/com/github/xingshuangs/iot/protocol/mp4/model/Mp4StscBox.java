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


import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample To Chunk Box(stbl-stsc),用chunk组织sample可以方便优化数据获取，一个chunk包含一个或多个sample。“stsc”中用一个表描述了
 * sample与chunk的映射关系，查看这张表就可以找到包含指定sample的thunk，从而找到这个sample，当然每个table entry可能包含一个或者多个chunk。fmp4方式，此box不必赋值，简单封装一个box即可
 *
 * @author xingshuang
 */
public class Mp4StscBox extends Mp4StcoBox {

    public Mp4StscBox() {
        this.mp4Type = EMp4Type.STSC;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 0;
    }
}
