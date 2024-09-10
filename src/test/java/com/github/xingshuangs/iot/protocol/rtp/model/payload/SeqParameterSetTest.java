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

package com.github.xingshuangs.iot.protocol.rtp.model.payload;

import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.*;


public class SeqParameterSetTest {

    @Test
    public void createSPS() {
        byte[] data = new byte[]{(byte) 0x67, (byte) 0x4d, (byte) 0x00, (byte) 0x1f, (byte) 0x96, (byte) 0x35,
                (byte) 0x40, (byte) 0xa0, (byte) 0x0b, (byte) 0x74, (byte) 0xdc, (byte) 0x04, (byte) 0x04,
                (byte) 0x04, (byte) 0x08};
        SeqParameterSet sps = SeqParameterSet.createSPS(data);
        assertEquals(77, sps.getProfileIdc());
        assertFalse(sps.isConstraintSet0Flag());
        assertFalse(sps.isConstraintSet1Flag());
        assertFalse(sps.isConstraintSet2Flag());
        assertFalse(sps.isConstraintSet3Flag());
        assertFalse(sps.isConstraintSet4Flag());
        assertFalse(sps.isConstraintSet5Flag());
        assertEquals(0, sps.getReservedZero2Bits());
        assertEquals(31, sps.getLevelIdc());
        assertEquals(0, sps.getSeqParameterSetId());
        assertEquals(4, sps.getLog2MaxFrameNumMinus4());
        assertEquals(0, sps.getPicOrderCntType());
        assertEquals(12, sps.getLog2MaxPicOrderCntKLsbMinus4());
        assertEquals(1, sps.getNumRefFrames());
        assertTrue(sps.isGapsInFrameNumValueAllowedFlag());
        assertEquals(79, sps.getPicWidthInMbsMinus1());
        assertEquals(44, sps.getPicHeightInMapUnitsMinus1());
        assertTrue(sps.isFrameMbsOnlyFlag());
        assertTrue(sps.isDirect8x8InferenceFlag());
        assertFalse(sps.isFrameCroppingFlag());
        assertTrue(sps.isVuiParametersPresentFlag());
        assertFalse(sps.isAspectRatioInfoPresentFlag());
        assertEquals(1280, sps.getWidth());
        assertEquals(720, sps.getHeight());
    }

    @Test
    public void demoTest(){
        String data = "Z2QAKKzZQHgCJ+XARAAAD6QAAu4CPGDGWA==";
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(data);
        SeqParameterSet sps = SeqParameterSet.createSPS(decode);
        assertEquals(1920, sps.getWidth());
        assertEquals(1080, sps.getHeight());
    }
}