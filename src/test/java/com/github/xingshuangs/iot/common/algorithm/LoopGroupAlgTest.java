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

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertEquals;


public class LoopGroupAlgTest {

    @Test
    public void loopExecute() {
        AtomicInteger length = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        LoopGroupAlg.loopExecute(100, 9, (off, len) -> {
            count.getAndIncrement();
            length.addAndGet(len);
        });
        assertEquals(12, count.get());
        assertEquals(100, length.get());

        length.set(0);
        count.set(0);
        LoopGroupAlg.loopExecute(120, 7, (off, len) -> {
            count.getAndIncrement();
            length.addAndGet(len);
        });
        assertEquals(18, count.get());
        assertEquals(120, length.get());
    }

    @Test
    public void biLoopExecute() {
        LoopGroupItem item1 = new LoopGroupItem(20);
        LoopGroupItem item2 = new LoopGroupItem(23);

        AtomicInteger length = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> i1.getLen() + i2.getLen() >= 13;
        LoopGroupAlg.biLoopExecute(item1, item2, biPredicate, (i1, i2) -> {
            length.addAndGet(i1.getLen());
            length.addAndGet(i2.getLen());
            count.getAndIncrement();
        });
        assertEquals(43, length.get());
        assertEquals(4, count.get());
    }

    @Test
    public void biLoopExecute1() {
        LoopGroupItem item1 = new LoopGroupItem(20);
        LoopGroupItem item2 = new LoopGroupItem(23);

        AtomicInteger length = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> i1.getLen() * 2 + i2.getLen() >= 14;
        LoopGroupAlg.biLoopExecute(item1, item2, biPredicate, (i1, i2) -> {
            length.addAndGet(i1.getLen());
            length.addAndGet(i2.getLen());
            count.getAndIncrement();
        });
        assertEquals(43, length.get());
        assertEquals(5, count.get());
    }
}