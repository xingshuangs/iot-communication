package com.github.xingshuangs.iot.protocol.s7.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class S7SequentialGroupAlgTest {

    @Test
    public void recombination() {
        //  1,50,65,  200, 322,    99,500,        44
        // |1,50,65,90|110,106|216|99,117|221|162,44|
        List<Integer> src = Arrays.asList(1, 50, 65, 200, 322, 99, 500, 44);
        List<S7ComGroup> recombination = S7SequentialGroupAlg.recombination(src, 240 - 14, 5);
        assertEquals(6, recombination.size());
    }

    @Test
    public void recombination1() {
        //  33,77,65,156,   322,      5,210,   11,95
        // |33,77,65,31|125,91|221|10,5,196|14,11,95|
        List<Integer> src = Arrays.asList(33, 77, 65, 156, 322, 5, 210, 11, 95);
        List<S7ComGroup> recombination = S7SequentialGroupAlg.recombination(src, 240 - 14, 5);
        assertEquals(5, recombination.size());
    }
}