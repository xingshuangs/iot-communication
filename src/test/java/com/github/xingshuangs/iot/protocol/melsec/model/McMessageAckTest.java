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

import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import org.junit.Test;

import static org.junit.Assert.*;


public class McMessageAckTest {

    @Test
    public void mcDeviceBatchReadAckData3E() {
        byte[] src = new byte[]{
                (byte) 0xD0, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_3E);
        McHeader3EAck header = (McHeader3EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_3E.getAckSubHeader(), header.getSubHeader());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(12, header.getDataLength());
        McAckData data = (McAckData) ack.getData();
        assertEquals(10, data.getData().length);
        byte[] dataBytes = new byte[]{
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        assertArrayEquals(dataBytes, data.getData());
    }

    @Test
    public void mcDeviceBatchWriteAckData3E() {
        byte[] src = new byte[]{
                (byte) 0xD0, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x02, 0x00, 0x00, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_3E);
        McHeader3EAck header = (McHeader3EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_3E.getAckSubHeader(), header.getSubHeader());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(2, header.getDataLength());
    }

    @Test
    public void mcDeviceBatchWriteAckData3EError() {
        byte[] src = new byte[]{
                (byte) 0xD0, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0B, 0x00, 0x00, 0x01, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00, 0x01, 0x04, 0x01, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_3E);
        McHeader3EAck header = (McHeader3EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_3E.getAckSubHeader(), header.getSubHeader());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(11, header.getDataLength());
        assertEquals(0x0100, header.getEndCode());

        McError4E3EData data = (McError4E3EData) ack.getData();
        McFrame4E3EAccessRoute errorRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, errorRoute.getNetworkNumber());
        assertEquals(0xFF, errorRoute.getPcNumber());
        assertEquals(0x03FF, errorRoute.getRequestDestModuleIoNumber());
        assertEquals(0, errorRoute.getRequestDestModuleStationNumber());
        assertEquals(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS, data.getCommand());
        assertEquals(0x0001, data.getSubcommand());
    }

    @Test
    public void mcDeviceBatchReadAckData4E() {
        byte[] src = new byte[]{
                (byte) 0xD4, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_4E);
        McHeader4EAck header = (McHeader4EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_4E.getAckSubHeader(), header.getSubHeader());
        assertEquals(0x0010, header.getSerialNumber());
        assertEquals(0x0000, header.getFixedNumber());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(12, header.getDataLength());
        McAckData data = (McAckData) ack.getData();
        assertEquals(10, data.getData().length);
        byte[] dataBytes = new byte[]{
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        assertArrayEquals(dataBytes, data.getData());
    }

    @Test
    public void mcDeviceBatchWriteAckData4EError() {
        byte[] src = new byte[]{
                (byte) 0xD4, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0B, 0x00, 0x00, 0x01, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00, 0x01, 0x04, 0x01, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_4E);
        McHeader4EAck header = (McHeader4EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_4E.getAckSubHeader(), header.getSubHeader());
        assertEquals(0x0010, header.getSerialNumber());
        assertEquals(0x0000, header.getFixedNumber());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(11, header.getDataLength());
        assertEquals(0x0100, header.getEndCode());

        McError4E3EData data = (McError4E3EData) ack.getData();
        McFrame4E3EAccessRoute errorRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, errorRoute.getNetworkNumber());
        assertEquals(0xFF, errorRoute.getPcNumber());
        assertEquals(0x03FF, errorRoute.getRequestDestModuleIoNumber());
        assertEquals(0, errorRoute.getRequestDestModuleStationNumber());
        assertEquals(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS, data.getCommand());
        assertEquals(0x0001, data.getSubcommand());
    }

    @Test
    public void mcDeviceBatchWriteAckData4E() {
        byte[] src = new byte[]{
                (byte) 0xD4, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x02, 0x00, 0x00, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_4E);
        McHeader4EAck header = (McHeader4EAck) ack.getHeader();
        assertEquals(EMcFrameType.FRAME_4E.getAckSubHeader(), header.getSubHeader());
        assertEquals(0x0010, header.getSerialNumber());
        assertEquals(0x0000, header.getFixedNumber());
        McFrame4E3EAccessRoute accessRoute = (McFrame4E3EAccessRoute) header.getAccessRoute();
        assertEquals(0, accessRoute.getNetworkNumber());
        assertEquals(0xFF, accessRoute.getPcNumber());
        assertEquals(0x03FF, accessRoute.getRequestDestModuleIoNumber());
        assertEquals(0, accessRoute.getRequestDestModuleStationNumber());
        assertEquals(2, header.getDataLength());
    }

    @Test
    public void mcDeviceBatchReadAckData1E() {
        byte[] src = new byte[]{
                (byte) 0x80, 0x00, 0x10, 0x10, 0x00, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_1E);
        McHeader1EAck header = (McHeader1EAck) ack.getHeader();
        assertEquals(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_BIT.getCode(), header.getSubHeader() - 0x80);
        assertEquals(0x00, header.getEndCode());
        McAckData data = (McAckData) ack.getData();
        assertEquals(4, data.getData().length);
        byte[] dataBytes = new byte[]{
                0x10, 0x10, 0x00, 0x00
        };
        assertArrayEquals(dataBytes, data.getData());
    }

    @Test
    public void mcDeviceBatchWriteAckData1E() {
        byte[] src = new byte[]{
                (byte) 0x82, 0x00
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_1E);
        McHeader1EAck header = (McHeader1EAck) ack.getHeader();
        assertEquals(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_BIT.getCode(), header.getSubHeader() - 0x80);
        assertEquals(0x00, header.getEndCode());
    }

    @Test
    public void mcDeviceBatchWriteAckData1EError() {
        byte[] src = new byte[]{
                (byte) 0x82, 0x01, 0x02
        };

        McMessageAck ack = McMessageAck.fromBytes(src, EMcFrameType.FRAME_1E);
        McHeader1EAck header = (McHeader1EAck) ack.getHeader();
        assertEquals(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_BIT.getCode(), header.getSubHeader() - 0x80);
        assertEquals(0x01, header.getEndCode());

        McError1EData data = (McError1EData) ack.getData();
        assertEquals(0x02, data.getError());
    }
}