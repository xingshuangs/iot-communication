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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class McReqBuilderTest {

    @Test
    public void createMcHeaderReq4E() {
        byte[] expect = new byte[]{
                0x50, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x00, 0x00, 0x0C, 0x00
        };
        McFrame4E3EAccessRoute route = McFrame4E3EAccessRoute.createDefault();
        McHeaderReq headerReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), route, 3000);
        byte[] actual = headerReq.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createReadDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.D, 7000, 2);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateReadDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0E, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.D, 7000, 2);
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(EMcSeries.IQ_R, header, deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createReadDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x01, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.D, 7000, 2);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInBitReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateReadDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0E, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcDeviceCode.D, 7000, 2);
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(EMcSeries.IQ_R, header, deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createWriteDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x12, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x03, 0x00, (byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11
        };
        byte[] data = new byte[]{(byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11};
        McDeviceContent deviceAddress = new McDeviceContent(EMcDeviceCode.D, 7000, 3, data);
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateWriteDeviceBatchInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x14, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x03, 0x00, (byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11
        };
        byte[] data = new byte[]{(byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11};
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McDeviceContent deviceAddress = new McDeviceContent(EMcDeviceCode.D, 7000, 3, data);
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInWordReq(EMcSeries.IQ_R, header, deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createWriteDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x10, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x01, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x08, 0x00, 0x11, 0x00, 0x11, 0x00
        };
        byte[] data = new byte[]{0x11, 0x00, 0x11, 0x00};
        McDeviceContent deviceAddress = new McDeviceContent(EMcDeviceCode.D, 7000, 8, data);
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInBitReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateWriteDeviceBatchInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x12, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x03, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x08, 0x00, 0x11, 0x00, 0x11, 0x00
        };
        byte[] data = new byte[]{0x11, 0x00, 0x11, 0x00};
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McDeviceContent deviceAddress = new McDeviceContent(EMcDeviceCode.D, 7000, 8, data);
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInBitReq(EMcSeries.IQ_R, header, deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createReadDeviceRandomInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x24, 0x00, 0x0C, 0x00,
                0x03, 0x04, 0x00, 0x00, 0x04, 0x03,
                0x00, 0x00, 0x00, (byte) 0xA8,
                0x00, 0x00, 0x00, (byte) 0xC2,
                0x64, 0x00, 0x00, (byte) 0x90,
                0x20, 0x00, 0x00, (byte) 0x9C,
                (byte) 0xDC, 0x05, 0x00, (byte) 0xA8,
                0x60, 0x01, 0x00, (byte) 0x9D,
                0x57, 0x04, 0x00, (byte) 0x90
        };
        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 0, 1));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.TN, 0, 1));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 100, 1));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.X, 0x20, 1));

        List<McDeviceAddress> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 1500, 1));
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.Y, 0x160, 1));
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 1111, 1));
        McMessageReq req = McReqBuilder.createReadDeviceRandomInWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateReadDeviceRandomInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x32, 0x00, 0x0C, 0x00,
                0x03, 0x04, 0x02, 0x00, 0x04, 0x03,
                0x00, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte) 0xC2, 0x00,
                0x64, 0x00, 0x00, 0x00, (byte) 0x90, 0x00,
                0x20, 0x00, 0x00, 0x00, (byte) 0x9C, 0x00,
                (byte) 0xDC, 0x05, 0x00, 0x00, (byte) 0xA8, 0x00,
                0x60, 0x01, 0x00, 0x00, (byte) 0x9D, 0x00,
                0x57, 0x04, 0x00, 0x00, (byte) 0x90, 0x00,
        };
        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 0));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.TN, 0));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 100));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.X, 0x20));

        List<McDeviceAddress> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 1500));
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.Y, 0x160));
        dwordAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 1111));
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createReadDeviceRandomInWordReq(EMcSeries.IQ_R, header, wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createWriteDeviceRandomInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x38, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x00, 0x00, 0x04, 0x03,
                0x00, 0x00, 0x00, (byte) 0xA8, 0x50, 0x05,
                0x01, 0x00, 0x00, (byte) 0xA8, 0x75, 0x05,
                0x64, 0x00, 0x00, (byte) 0x90, 0x40, 0x05,
                0x20, 0x00, 0x00, (byte) 0x9C, (byte) 0x83, 0x05,
                (byte) 0xDC, 0x05, 0x00, (byte) 0xA8, 0x02, 0x12, 0x39, 0x04,
                0x60, 0x01, 0x00, (byte) 0x9D, 0x07, 0x26, 0x75, 0x23,
                0x57, 0x04, 0x00, (byte) 0x90, 0x75, 0x04, 0x25, 0x04
        };
        List<McDeviceContent> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 0, new byte[]{0x50, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 1, new byte[]{0x75, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.M, 100, new byte[]{0x40, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.X, 0x20, new byte[]{(byte) 0x83, 0x05}));

        List<McDeviceContent> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 1500, new byte[]{0x02, 0x12, 0x39, 0x04}));
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.Y, 0x160, new byte[]{0x07, 0x26, 0x75, 0x23}));
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.M, 1111, new byte[]{0x75, 0x04, 0x25, 0x04}));
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateWriteDeviceRandomInWordReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x46, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x02, 0x00, 0x04, 0x03,
                0x00, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00, 0x50, 0x05,
                0x01, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00, 0x75, 0x05,
                0x64, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x40, 0x05,
                0x20, 0x00, 0x00, 0x00, (byte) 0x9C, 0x00, (byte) 0x83, 0x05,
                (byte) 0xDC, 0x05, 0x00, 0x00, (byte) 0xA8, 0x00, 0x02, 0x12, 0x39, 0x04,
                0x60, 0x01, 0x00, 0x00, (byte) 0x9D, 0x00, 0x07, 0x26, 0x75, 0x23,
                0x57, 0x04, 0x00, 0x00, (byte) 0x90, 0x00, 0x75, 0x04, 0x25, 0x04
        };
        List<McDeviceContent> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 0, new byte[]{0x50, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 1, new byte[]{0x75, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.M, 100, new byte[]{0x40, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.X, 0x20, new byte[]{(byte) 0x83, 0x05}));

        List<McDeviceContent> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 1500, new byte[]{0x02, 0x12, 0x39, 0x04}));
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.Y, 0x160, new byte[]{0x07, 0x26, 0x75, 0x23}));
        dwordAddresses.add(new McDeviceContent(EMcDeviceCode.M, 1111, new byte[]{0x75, 0x04, 0x25, 0x04}));
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInWordReq(EMcSeries.IQ_R, header, wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createWriteDeviceRandomInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x11, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x01, 0x00, 0x02,
                0x32, 0x00, 0x00, (byte) 0x90, 0x00,
                0x2F, 0x00, 0x00, (byte) 0x9D, 0x01,
        };
        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 50, new byte[]{0x00}));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.Y, 0x2F, new byte[]{0x01}));
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInBitReq(bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateWriteDeviceRandomInBitReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x15, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x03, 0x00, 0x02,
                0x32, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x00,
                0x2F, 0x00, 0x00, 0x00, (byte) 0x9D, 0x00, 0x01,
        };
        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 50, new byte[]{0x00}));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.Y, 0x2F, new byte[]{0x01}));
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createWriteDeviceRandomInBitReq(EMcSeries.IQ_R, header, bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createReadDeviceBatchMultiBlocksReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x26, 0x00, 0x0C, 0x00,
                0x06, 0x04, 0x00, 0x00, 0x02, 0x03,
                0x00, 0x00, 0x00, (byte) 0xA8, 0x04, 0x00,
                0x00, 0x01, 0x00, (byte) 0xB4, 0x08, 0x00,
                0x00, 0x00, 0x00, (byte) 0x90, 0x02, 0x00,
                (byte) 0x80, 0x00, 0x00, (byte) 0x90, 0x02, 0x00,
                0x00, 0x01, 0x00, (byte) 0xA0, 0x03, 0x00
        };
        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 0, 4));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.W, 0x100, 8));

        List<McDeviceAddress> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 0, 2));
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 128, 2));
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.B, 0x100, 3));
        McMessageReq req = McReqBuilder.createReadDeviceBatchMultiBlocksReq(wordAddresses, bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateReadDeviceBatchMultiBlocksReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x30, 0x00, 0x0C, 0x00,
                0x06, 0x04, 0x02, 0x00, 0x02, 0x03,
                0x00, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00, 0x04, 0x00,
                0x00, 0x01, 0x00, 0x00, (byte) 0xB4, 0x00, 0x08, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x02, 0x00,
                (byte) 0x80, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x02, 0x00,
                0x00, 0x01, 0x00, 0x00, (byte) 0xA0, 0x00, 0x03, 0x00
        };
        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.D, 0, 4));
        wordAddresses.add(new McDeviceAddress(EMcDeviceCode.W, 0x100, 8));

        List<McDeviceAddress> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 0, 2));
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.M, 128, 2));
        bitAddresses.add(new McDeviceAddress(EMcDeviceCode.B, 0x100, 3));
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createReadDeviceBatchMultiBlocksReq(EMcSeries.IQ_R, header, wordAddresses, bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createWriteDeviceBatchMultiBlocksReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x4C, 0x00, 0x0C, 0x00,
                0x06, 0x14, 0x00, 0x00, 0x02, 0x03,
                0x00, 0x00, 0x00, (byte) 0xA8, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, (byte) 0xB4, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0x90, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0x80, 0x00, 0x00, (byte) 0x90, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, (byte) 0xA0, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };
        List<McDeviceContent> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 0, 4, new byte[8]));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.W, 0x100, 8, new byte[16]));

        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 0, 2, new byte[4]));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 128, 2, new byte[4]));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.B, 0x100, 3, new byte[6]));
        McMessageReq req = McReqBuilder.createWriteDeviceBatchMultiBlocksReq(wordAddresses, bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateWriteDeviceBatchMultiBlocksReq() {
        byte[] expect = new byte[]{
                0x50, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x56, 0x00, 0x0C, 0x00,
                0x06, 0x14, 0x02, 0x00, 0x02, 0x03,
                0x00, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00, (byte) 0xB4, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0x80, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00, (byte) 0xA0, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };
        List<McDeviceContent> wordAddresses = new ArrayList<>();
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.D, 0, 4, new byte[8]));
        wordAddresses.add(new McDeviceContent(EMcDeviceCode.W, 0x100, 8, new byte[16]));

        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 0, 2, new byte[4]));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.M, 128, 2, new byte[4]));
        bitAddresses.add(new McDeviceContent(EMcDeviceCode.B, 0x100, 3, new byte[6]));
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(), McFrame4E3EAccessRoute.createDefault(), 3000);
        McMessageReq req = McReqBuilder.createWriteDeviceBatchMultiBlocksReq(EMcSeries.IQ_R, header, wordAddresses, bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }
}