package com.github.xingshuangs.iot.protocol.melsec.model;

import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class McReqBuilderTest {

    @Test
    public void createMcHeaderReq4E() {
        byte[] expect = new byte[]{
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x00, 0x00, 0x0C, 0x00
        };
        Mc4EFrameAccessRoute route = Mc4EFrameAccessRoute.createDefault();
        McHeaderReq headerReq = McReqBuilder.createMcHeaderReq4E(route, 3000);
        byte[] actual = headerReq.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceBatchReadWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(7000, EMcDeviceCode.D, 2);
        McMessageReq req = McReqBuilder.createDeviceBatchReadWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceBatchReadWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0E, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcSeries.IQ_R, 7000, EMcDeviceCode.D, 2);
        McMessageReq req = McReqBuilder.createDeviceBatchReadWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceBatchReadBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0C, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x01, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(7000, EMcDeviceCode.D, 2);
        McMessageReq req = McReqBuilder.createDeviceBatchReadBitReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceBatchReadBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x0E, 0x00, 0x0C, 0x00,
                0x01, 0x04, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x02, 0x00
        };
        McDeviceAddress deviceAddress = new McDeviceAddress(EMcSeries.IQ_R, 7000, EMcDeviceCode.D, 2);
        McMessageReq req = McReqBuilder.createDeviceBatchReadWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceBatchWriteWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x12, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x00, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x03, 0x00, (byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11
        };
        byte[] data = new byte[]{(byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11};
        McDeviceContent deviceAddress = new McDeviceContent(7000, EMcDeviceCode.D, 3, data);
        McMessageReq req = McReqBuilder.createDeviceBatchWriteWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceBatchWriteWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x14, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x02, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x03, 0x00, (byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11
        };
        byte[] data = new byte[]{(byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11};
        McDeviceContent deviceAddress = new McDeviceContent(EMcSeries.IQ_R, 7000, EMcDeviceCode.D, 3, data);
        McMessageReq req = McReqBuilder.createDeviceBatchWriteWordReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceBatchWriteBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x10, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x01, 0x00,
                0x58, 0x1B, 0x00, (byte) 0xA8, 0x08, 0x00, 0x11, 0x00, 0x11, 0x00
        };
        byte[] data = new byte[]{0x11, 0x00, 0x11, 0x00};
        McDeviceContent deviceAddress = new McDeviceContent(7000, EMcDeviceCode.D, 8, data);
        McMessageReq req = McReqBuilder.createDeviceBatchWriteBitReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceBatchWriteBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x12, 0x00, 0x0C, 0x00,
                0x01, 0x14, 0x03, 0x00,
                0x58, 0x1B, 0x00, 0x00, (byte) 0xA8, 0x00, 0x08, 0x00, 0x11, 0x00, 0x11, 0x00
        };
        byte[] data = new byte[]{0x11, 0x00, 0x11, 0x00};
        McDeviceContent deviceAddress = new McDeviceContent(EMcSeries.IQ_R, 7000, EMcDeviceCode.D, 8, data);
        McMessageReq req = McReqBuilder.createDeviceBatchWriteBitReq(deviceAddress);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceRandomReadWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
        wordAddresses.add(new McDeviceAddress(0, EMcDeviceCode.D, 1));
        wordAddresses.add(new McDeviceAddress(0, EMcDeviceCode.TN, 1));
        wordAddresses.add(new McDeviceAddress(100, EMcDeviceCode.M, 1));
        wordAddresses.add(new McDeviceAddress(0x20, EMcDeviceCode.X, 1));

        List<McDeviceAddress> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceAddress(1500, EMcDeviceCode.D, 1));
        dwordAddresses.add(new McDeviceAddress(0x160, EMcDeviceCode.Y, 1));
        dwordAddresses.add(new McDeviceAddress(1111, EMcDeviceCode.M, 1));
        McMessageReq req = McReqBuilder.createDeviceRandomReadWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceRandomReadWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
        wordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 0, EMcDeviceCode.D));
        wordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 0, EMcDeviceCode.TN));
        wordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 100, EMcDeviceCode.M));
        wordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 0x20, EMcDeviceCode.X));

        List<McDeviceAddress> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 1500, EMcDeviceCode.D));
        dwordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 0x160, EMcDeviceCode.Y));
        dwordAddresses.add(new McDeviceAddress(EMcSeries.IQ_R, 1111, EMcDeviceCode.M));
        McMessageReq req = McReqBuilder.createDeviceRandomReadWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceRandomWriteWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
        wordAddresses.add(new McDeviceContent(0, EMcDeviceCode.D, new byte[]{0x50, 0x05}));
        wordAddresses.add(new McDeviceContent(1, EMcDeviceCode.D, new byte[]{0x75, 0x05}));
        wordAddresses.add(new McDeviceContent(100, EMcDeviceCode.M, new byte[]{0x40, 0x05}));
        wordAddresses.add(new McDeviceContent(0x20, EMcDeviceCode.X, new byte[]{(byte) 0x83, 0x05}));

        List<McDeviceContent> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceContent(1500, EMcDeviceCode.D, new byte[]{0x02, 0x12, 0x39, 0x04}));
        dwordAddresses.add(new McDeviceContent(0x160, EMcDeviceCode.Y, new byte[]{0x07, 0x26, 0x75, 0x23}));
        dwordAddresses.add(new McDeviceContent(1111, EMcDeviceCode.M, new byte[]{0x75, 0x04, 0x25, 0x04}));
        McMessageReq req = McReqBuilder.createDeviceRandomWriteWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceRandomWriteWordReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
        wordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 0, EMcDeviceCode.D, new byte[]{0x50, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 1, EMcDeviceCode.D, new byte[]{0x75, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 100, EMcDeviceCode.M, new byte[]{0x40, 0x05}));
        wordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 0x20, EMcDeviceCode.X, new byte[]{(byte) 0x83, 0x05}));

        List<McDeviceContent> dwordAddresses = new ArrayList<>();
        dwordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 1500, EMcDeviceCode.D, new byte[]{0x02, 0x12, 0x39, 0x04}));
        dwordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 0x160, EMcDeviceCode.Y, new byte[]{0x07, 0x26, 0x75, 0x23}));
        dwordAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 1111, EMcDeviceCode.M, new byte[]{0x75, 0x04, 0x25, 0x04}));
        McMessageReq req = McReqBuilder.createDeviceRandomWriteWordReq(wordAddresses, dwordAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceRandomWriteBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x11, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x01, 0x00, 0x02,
                0x32, 0x00, 0x00, (byte) 0x90, 0x00,
                0x2F, 0x00, 0x00, (byte) 0x9D, 0x01,
        };
        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(50, EMcDeviceCode.M, new byte[]{0x00}));
        bitAddresses.add(new McDeviceContent(0x2F, EMcDeviceCode.Y, new byte[]{0x01}));
        McMessageReq req = McReqBuilder.createDeviceRandomWriteBitReq(bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void testCreateDeviceRandomWriteBitReq() {
        byte[] expect = new byte[]{
                0x54, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
                0x15, 0x00, 0x0C, 0x00,
                0x02, 0x14, 0x03, 0x00, 0x02,
                0x32, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x00,
                0x2F, 0x00, 0x00, 0x00, (byte) 0x9D, 0x00, 0x01,
        };
        List<McDeviceContent> bitAddresses = new ArrayList<>();
        bitAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 50, EMcDeviceCode.M, new byte[]{0x00}));
        bitAddresses.add(new McDeviceContent(EMcSeries.IQ_R, 0x2F, EMcDeviceCode.Y, new byte[]{0x01}));
        McMessageReq req = McReqBuilder.createDeviceRandomWriteBitReq(bitAddresses);
        byte[] actual = req.toByteArray();
        assertArrayEquals(expect, actual);
    }

    @Test
    public void createDeviceBatchReadMultiBlocksReq() {
    }

    @Test
    public void testCreateDeviceBatchReadMultiBlocksReq() {
    }

    @Test
    public void createDeviceBatchWriteMultiBlocksReq() {
    }

    @Test
    public void testCreateDeviceBatchWriteMultiBlocksReq() {
    }
}