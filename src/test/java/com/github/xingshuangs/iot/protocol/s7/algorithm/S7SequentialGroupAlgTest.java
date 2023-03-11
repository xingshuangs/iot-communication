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
    public void readRecombination() {
        // 1, 9, 102, 33, 2, 4, 8, 326, 2, 2, 2, 2, 2, 400, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99
        // 1, 9, 102, 33, 2, 4, 8, 13| 221| 92, 2, 2, 2, 2, 2, 64|221|115, 2, 2, 2, 2, 2, 2, 2, 2| 2, 2, 2, 99
        int[] data = new int[]{1, 9, 102, 33, 2, 4, 8, 326, 2, 2, 2, 2, 2, 400, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99};
        int[] expect = new int[]{1, 9, 102, 33, 2, 4, 8, 13, 221, 92, 2, 2, 2, 2, 2, 64, 221, 115, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 99};
        List<Integer> src = Arrays.stream(data).boxed().collect(Collectors.toList());
        List<S7ComGroup> recombination = S7SequentialGroupAlg.readRecombination(src, 240 - 14, 5,12);
        int[] actual = recombination.stream().flatMap(x -> x.getItems().stream())
                .mapToInt(S7ComItem::getRipeSize).toArray();
        assertArrayEquals(expect, actual);
//        System.out.println(Arrays.toString(actual));
//        recombination.forEach(x->{
//            x.getItems().forEach(y-> System.out.println(y.toString() + y.getTotalLength()));
//            System.out.println("----------------------------------------------------");
//        });
    }
}