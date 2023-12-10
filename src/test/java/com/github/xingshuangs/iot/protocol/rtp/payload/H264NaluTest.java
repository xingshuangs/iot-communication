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

package com.github.xingshuangs.iot.protocol.rtp.payload;

import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluFuA;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluSingle;
import org.junit.Test;

import static org.junit.Assert.*;


public class H264NaluTest {

    @Test
    public void h264NaluFuA_Start() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x85, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertTrue(nalu.getFuHeader().isStart());
        assertFalse(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluFuA_Middle() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertFalse(nalu.getFuHeader().isStart());
        assertFalse(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluFuA_End() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x45, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertFalse(nalu.getFuHeader().isStart());
        assertTrue(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluSingle() {
        byte[] expect = new byte[]{(byte) 0x61, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluSingle nalu = H264NaluSingle.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.NON_IDR_SLICE, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());
    }
}