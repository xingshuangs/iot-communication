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

package com.github.xingshuangs.iot.protocol.rtp.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class RtpPackageTest {

    @Test
    public void fromBytes() {
        byte[] expect = new byte[]{
                (byte) 0xA0, (byte) 0x60, (byte) 0x80, (byte) 0x38, (byte) 0xE0, (byte) 0xE6, (byte) 0x24, (byte) 0xFA,
                (byte) 0x3A, (byte) 0x64, (byte) 0xE1, (byte) 0xBA, (byte) 0x67, (byte) 0x4D, (byte) 0x00, (byte) 0x1F,
                (byte) 0x96, (byte) 0x35, (byte) 0x40, (byte) 0xA0, (byte) 0x0B, (byte) 0x74, (byte) 0xDC, (byte) 0x04,
                (byte) 0xA04, (byte) 0x04, (byte) 0x80, (byte) 0x01
        };
        byte[] payload = new byte[]{
                (byte) 0x67, (byte) 0x4D, (byte) 0x00, (byte) 0x1F, (byte) 0x96, (byte) 0x35, (byte) 0x40, (byte) 0xA0,
                (byte) 0x0B, (byte) 0x74, (byte) 0xDC, (byte) 0x04, (byte) 0xA04, (byte) 0x04, (byte) 0x80
        };
        RtpPackage rtp = RtpPackage.fromBytes(expect);
        assertEquals(2, rtp.getHeader().getVersion());
        assertTrue(rtp.getHeader().isPadding());
        assertFalse(rtp.getHeader().isExtension());
        assertEquals(0, rtp.getHeader().getCsrcCount());
        assertFalse(rtp.getHeader().isMarker());
        assertEquals(96, rtp.getHeader().getPayloadType());
        assertEquals(32824, rtp.getHeader().getSequenceNumber());
        assertEquals(3773179130L, rtp.getHeader().getTimestamp());
        assertEquals(979689914, rtp.getHeader().getSsrc());
        assertEquals(0, rtp.getHeader().getExtensionHeaderId());
        assertEquals(0, rtp.getHeader().getExtensionHeaderLength());
        assertArrayEquals(payload, rtp.getPayload());
        assertEquals(1, rtp.getIgnoreLength());
    }

    @Test
    public void fromBytes1() {
        byte[] expect = new byte[]{
                (byte) 0xA0, (byte) 0x60, (byte) 0x80, (byte) 0x3A, (byte) 0xE0, (byte) 0xE6, (byte) 0x24, (byte) 0xFA,
                (byte) 0x3A, (byte) 0x64, (byte) 0xE1, (byte) 0xBA, (byte) 0x06, (byte) 0xE5, (byte) 0x01, (byte) 0x34,
                (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x03
        };
        byte[] payload = new byte[]{(byte) 0x06, (byte) 0xE5, (byte) 0x01, (byte) 0x34, (byte) 0x80};
        RtpPackage rtp = RtpPackage.fromBytes(expect);
        assertEquals(2, rtp.getHeader().getVersion());
        assertTrue(rtp.getHeader().isPadding());
        assertFalse(rtp.getHeader().isExtension());
        assertEquals(0, rtp.getHeader().getCsrcCount());
        assertFalse(rtp.getHeader().isMarker());
        assertEquals(96, rtp.getHeader().getPayloadType());
        assertEquals(32826, rtp.getHeader().getSequenceNumber());
        assertEquals(3773179130L, rtp.getHeader().getTimestamp());
        assertEquals(979689914, rtp.getHeader().getSsrc());
        assertEquals(0, rtp.getHeader().getExtensionHeaderId());
        assertEquals(0, rtp.getHeader().getExtensionHeaderLength());
        assertArrayEquals(payload, rtp.getPayload());
        assertEquals(3, rtp.getIgnoreLength());
    }
}