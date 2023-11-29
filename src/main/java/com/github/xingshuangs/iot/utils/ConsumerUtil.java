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

package com.github.xingshuangs.iot.utils;


import java.util.function.BiConsumer;

/**
 * 消费相关工具
 *
 * @author xingshuang
 */
public class ConsumerUtil {

    private ConsumerUtil() {
        // NOOP
    }

    /**
     * 循环执行
     *
     * @param actualLength 实际长度
     * @param maxLength    最大允许长度
     * @param biConsumer   消费执行
     */
    public static void loopExecute(int actualLength, int maxLength, BiConsumer<Integer, Integer> biConsumer) {
        // 索引偏移
        int off = 0;
        while (off < actualLength) {
            int len = maxLength <= 0 ? actualLength - off : Math.min(maxLength, actualLength - off);
            biConsumer.accept(off, len);
            off += len;
        }
    }
}
