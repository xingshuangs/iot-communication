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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class S7SequentialGroupAlgTest {

    @Test
    public void writeRecombination() {
        //  1,50,65,  200, 322,    99,500,        44
        // |1,50,65,90|110,106|216|99,117|221|162,44|
        int[] data = new int[]{1, 50, 65, 200, 322, 99, 500, 44};
        int[] expect = new int[]{1, 50, 65, 90, 110, 106, 216, 99, 117, 221, 162, 44};
        List<Integer> src = Arrays.stream(data).boxed().collect(Collectors.toList());
        List<S7ComGroup> recombination = S7SequentialGroupAlg.writeRecombination(src, 240 - 14, 5);
        assertEquals(6, recombination.size());
        int[] actual = recombination.stream().flatMap(x -> x.getItems().stream())
                .mapToInt(S7ComItem::getRipeSize).toArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void writeRecombination1() {
        int[] data = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        int[] expect = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        List<Integer> src = Arrays.stream(data).boxed().collect(Collectors.toList());
        List<S7ComGroup> recombination = S7SequentialGroupAlg.writeRecombination(src, 228, 17);
        int[] actual = recombination.stream().flatMap(x -> x.getItems().stream())
                .mapToInt(S7ComItem::getRipeSize).toArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void readRecombination() {
        // 1, 9, 102, 33, 2, 4, 8, 326, 2, 2, 2, 2, 2, 400, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99
        // 1, 9, 102, 33, 2, 4, 8, 13| 221| 92, 2, 2, 2, 2, 2, 64|221|115, 2, 2, 2, 2, 2, 2, 2, 2| 2, 2, 2, 99
        int[] data = new int[]{1, 9, 102, 33, 2, 4, 8, 326, 2, 2, 2, 2, 2, 400, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99};
        int[] expect = new int[]{1, 9, 102, 33, 2, 4, 8, 13, 221, 92, 2, 2, 2, 2, 2, 64, 221, 115, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99};
        List<Integer> src = Arrays.stream(data).boxed().collect(Collectors.toList());
        List<S7ComGroup> recombination = S7SequentialGroupAlg.readRecombination(src, 240 - 14, 5, 12);
        int[] actual = recombination.stream().flatMap(x -> x.getItems().stream())
                .mapToInt(S7ComItem::getRipeSize).toArray();
        assertArrayEquals(expect, actual);
//        System.out.println(Arrays.toString(actual));
//        recombination.forEach(x->{
//            x.getItems().forEach(y-> System.out.println(y.toString() + y.getTotalLength()));
//            System.out.println("----------------------------------------------------");
//        });
    }

    @Test
    public void readRecombination1() {
        int[] data = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        int[] expect = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        List<Integer> src = Arrays.stream(data).boxed().collect(Collectors.toList());
        List<S7ComGroup> recombination = S7SequentialGroupAlg.readRecombination(src, 240 - 14, 5, 12);
        int[] actual = recombination.stream().flatMap(x -> x.getItems().stream())
                .mapToInt(S7ComItem::getRipeSize).toArray();
        assertArrayEquals(expect, actual);
    }
}