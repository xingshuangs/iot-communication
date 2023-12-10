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

package com.github.xingshuangs.iot.protocol.melsec.model;

import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import org.junit.Test;

import static org.junit.Assert.*;


public class McHeaderAckTest {

    @Test
    public void fromBytes4E() {

        byte[] src = new byte[]{
                (byte)0xD4, 0x00, 0x34, 0x12, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x00, 0x00
        };

        McHeaderAck ack = McHeaderAck.fromBytes(src, EMcFrameType.FRAME_4E);
        assertEquals(EMcFrameType.FRAME_4E.getAckSubHeader(), ack.getSubHeader());
        assertEquals(4660, ack.getSerialNumber());
        assertEquals(0, ack.getFixedNumber());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) ack.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(12, ack.getDataLength());
    }

    @Test
    public void fromBytes3E() {

        byte[] src = new byte[]{
                (byte)0xD0, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x00, 0x00
        };

        McHeaderAck ack = McHeaderAck.fromBytes(src, EMcFrameType.FRAME_3E);
        assertEquals(EMcFrameType.FRAME_3E.getAckSubHeader(), ack.getSubHeader());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) ack.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(12, ack.getDataLength());
    }
}