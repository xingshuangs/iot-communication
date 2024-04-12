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

import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.ShortUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class McMessageReqASeriesTest {

    @Test
    public void createReadDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x01, (byte) 0xFF,
                0x0A, 0x00,
                0x32, 0x00, 0x00, 0x00,
                0x20, 0x44, 0x01, 0x00
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.D, 50, 1);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(EMcSeries.A, header, deviceAddress);
        assertArrayEquals(expect, req.toByteArray());
    }

    @Test
    public void createReadDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x00, (byte) 0xFF,
                0x0A, 0x00,
                0x32, 0x00, 0x00, 0x00,
                0x20, 0x58, 0x01, 0x00
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.X, 50, 1);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInBitReq(EMcSeries.A, header, deviceAddress);
        assertArrayEquals(expect, req.toByteArray());
    }

    @Test
    public void createWriteDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x03, (byte) 0xFF,
                0x0A, 0x00,
                0x32, 0x00, 0x00, 0x00,
                0x20, 0x44, 0x01, 0x00, 0x01, 0x00
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        McDeviceContent deviceContent = new McDeviceContent(EMcDeviceCode.D, 50, 1, ShortUtil.toByteArray(1, true));
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInWordReq(EMcSeries.A, header, deviceContent);
        assertArrayEquals(expect, req.toByteArray());
    }

    @Test
    public void createWriteDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x02, (byte) 0xFF,
                0x0A, 0x00,
                0x32, 0x00, 0x00, 0x00,
                0x20, 0x58, 0x01, 0x00, 0x01, 0x00
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        McDeviceContent deviceContent = new McDeviceContent(EMcDeviceCode.X, 50, 1, ShortUtil.toByteArray(1, true));
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInBitReq(EMcSeries.A, header, deviceContent);
        assertArrayEquals(expect, req.toByteArray());
    }

    @Test
    public void createWriteDeviceRandomInWordReq() {
        byte[] expect = new byte[]{
                0x05, (byte) 0xFF,
                0x0A, 0x00,
                0x02, 0x00,
                0x32, 0x00, 0x00, 0x00, 0x20, 0x44, 0x01, 0x00,
                0x33, 0x00, 0x00, 0x00, 0x20, 0x44, 0x01, 0x00,
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        List<McDeviceContent> list = new ArrayList<>();
        list.add(new McDeviceContent(EMcDeviceCode.D, 50, 1, ShortUtil.toByteArray(1, true)));
        list.add(new McDeviceContent(EMcDeviceCode.D, 51, 1, ShortUtil.toByteArray(1, true)));
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInWordReq(EMcSeries.A, header, list, new ArrayList<>());
        assertArrayEquals(expect, req.toByteArray());
    }

    @Test
    public void createWriteDeviceRandomInBitReq() {
        byte[] expect = new byte[]{
                0x04, (byte) 0xFF,
                0x0A, 0x00,
                0x02, 0x00,
                0x32, 0x00, 0x00, 0x00, 0x20, 0x58, 0x01,
                0x33, 0x00, 0x00, 0x00, 0x20, 0x58, 0x00
        };
        McFrame1EAccessRoute route = new McFrame1EAccessRoute();
        McHeaderReq header = new McHeader1EReq(route, 2500);
        List<McDeviceContent> list = new ArrayList<>();
        list.add(new McDeviceContent(EMcDeviceCode.X, 50, 1, new byte[]{0x01}));
        list.add(new McDeviceContent(EMcDeviceCode.X, 51, 1, new byte[]{0x00}));
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInBitReq(EMcSeries.A, header, list);
        assertArrayEquals(expect, req.toByteArray());
    }
}