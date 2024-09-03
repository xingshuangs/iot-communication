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

package com.github.xingshuangs.iot.common.algorithm;


import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Loop execute by group algorithm utility class
 * (循环处理相关工具)
 *
 * @author xingshuang
 */
public class LoopGroupAlg {

    private LoopGroupAlg() {
        // NOOP
    }

    /**
     * Loop execute each item.
     * (循环执行)
     *
     * @param actualLength actual length
     * @param maxLength    the maximum length allowed in each loop
     * @param biConsumer   custom handler with two parameters, first is current offset, second is current length.
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

    /**
     * Loop execute by condition and order, input two group items.
     * 寻找满足条件的索引和长度，存在两个组
     *
     * @param item1       group item1
     * @param item2       group item2
     * @param biPredicate custom condition
     * @param biConsumer  custom handler with two items, include different offset and length.
     */
    public static void biLoopExecute(LoopGroupItem item1, LoopGroupItem item2,
                                     BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate,
                                     BiConsumer<LoopGroupItem, LoopGroupItem> biConsumer) {

        while (item1.inRange() || item2.inRange()) {
            if (item1.getOff() < item1.getActualLength() && item1.inRange()) {
                // 全在第1项中
                if (biPredicate.test(item1, item2)) {
                    biConsumer.accept(item1, item2);
                    item1.setOff(item1.getOff() + item1.getLen());
                    item1.setLen(0);
                } else {
                    item1.setLen(item1.getLen() + 1);
                }
            } else if (item1.getOff() < item1.getActualLength() && !item1.inRange() && item2.inRange()) {
                // 既在第1项中，又在第2项中
                if (biPredicate.test(item1, item2)) {
                    biConsumer.accept(item1, item2);
                    item1.setOff(item1.getActualLength());
                    item1.setLen(0);
                    item2.setOff(item2.getOff() + item2.getLen());
                    item2.setLen(0);
                } else {
                    item2.setLen(item2.getLen() + 1);
                }
            } else if (item2.getOff() < item2.getActualLength() && item2.inRange()) {
                // 全在第2项中
                if (biPredicate.test(item1, item2)) {
                    biConsumer.accept(item1, item2);
                    item2.setOff(item2.getOff() + item2.getLen());
                    item2.setLen(0);
                } else {
                    item2.setLen(item2.getLen() + 1);
                }
            } else {
                item2.setLen(item2.getLen() + 1);
            }
        }
        if (item1.getOff() < item1.getActualLength() || item2.getOff() < item2.getActualLength()) {
            biConsumer.accept(item1, item2);
        }
    }
}
