package com.github.xingshuangs.iot.protocol.melsec.model;

import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import org.junit.Test;

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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
                0x54, 0x00,
                0x00, (byte) 0xFF, (byte) 0xFF, 0x03, 0x00,
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
    }

    @Test
    public void testCreateDeviceBatchWriteBitReq() {
    }

    @Test
    public void createDeviceRandomReadWordReq() {
    }

    @Test
    public void testCreateDeviceRandomReadWordReq() {
    }

    @Test
    public void createDeviceRandomWriteWordReq() {
    }

    @Test
    public void testCreateDeviceRandomWriteWordReq() {
    }

    @Test
    public void createDeviceRandomWriteBitReq() {
    }

    @Test
    public void testCreateDeviceRandomWriteBitReq() {
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