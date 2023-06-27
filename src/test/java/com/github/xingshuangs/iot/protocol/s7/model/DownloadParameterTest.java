package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class DownloadParameterTest {

    @Test
    public void startDownloadParameterTest() {

        byte[] expect = new byte[]{
                0x1A, 0x00, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x09, 0x5F, 0x30, 0x41,
                0x30, 0x30, 0x30, 0x30, 0x31, 0x50,
                0x0D, 0x31, 0x30, 0x30, 0x30, 0x35, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x34, 0x30, 0x30,
        };

        StartDownloadParameter parameter = new StartDownloadParameter();
        parameter.setFunctionCode(EFunctionCode.START_DOWNLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setUnknownBytes(new byte[]{0x01, 0x00});
        parameter.setDownloadId(0);
        parameter.setFileNameLength(9);
        parameter.setFileIdentifier("_");
        parameter.setBlockType(EFileBlockType.DB);
        parameter.setBlockNumber(1);
        parameter.setDestinationFileSystem(EDestinationFileSystem.P);
        parameter.setPart2Length(13);
        parameter.setUnknownChar("1");
        parameter.setLoadMemoryLength(500);
        parameter.setMC7CodeLength(400);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = StartDownloadParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_DOWNLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x01, 0x00}, parameter.getUnknownBytes());
        assertEquals(0, parameter.getDownloadId());
        assertEquals(9, parameter.getFileNameLength());
        assertEquals("_", parameter.getFileIdentifier());
        assertEquals(EFileBlockType.DB, parameter.getBlockType());
        assertEquals(1, parameter.getBlockNumber());
        assertEquals(EDestinationFileSystem.P, parameter.getDestinationFileSystem());
        assertEquals(13, parameter.getPart2Length());
        assertEquals("1", parameter.getUnknownChar());
        assertEquals(500, parameter.getLoadMemoryLength());
        assertEquals(400, parameter.getMC7CodeLength());

    }

    @Test
    public void downloadParameterTest() {

        byte[] expect = new byte[]{
                0x1A, 0x00, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x09, 0x5F, 0x30, 0x41,
                0x30, 0x30, 0x30, 0x30, 0x31, 0x50,
        };

        DownloadParameter parameter = new DownloadParameter();
        parameter.setFunctionCode(EFunctionCode.START_DOWNLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setUnknownBytes(new byte[]{0x01, 0x00});
        parameter.setDownloadId(0);
        parameter.setFileNameLength(9);
        parameter.setFileIdentifier("_");
        parameter.setBlockType(EFileBlockType.DB);
        parameter.setBlockNumber(1);
        parameter.setDestinationFileSystem(EDestinationFileSystem.P);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = DownloadParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_DOWNLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x01, 0x00}, parameter.getUnknownBytes());
        assertEquals(0, parameter.getDownloadId());
        assertEquals(9, parameter.getFileNameLength());
        assertEquals("_", parameter.getFileIdentifier());
        assertEquals(EFileBlockType.DB, parameter.getBlockType());
        assertEquals(1, parameter.getBlockNumber());
        assertEquals(EDestinationFileSystem.P, parameter.getDestinationFileSystem());

    }

    @Test
    public void downloadAckParameterTest() {

        byte[] expect = new byte[]{
                0x1A, 0x00
        };

        DownloadAckParameter parameter = new DownloadAckParameter();
        parameter.setFunctionCode(EFunctionCode.START_DOWNLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = DownloadAckParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_DOWNLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());

    }
}