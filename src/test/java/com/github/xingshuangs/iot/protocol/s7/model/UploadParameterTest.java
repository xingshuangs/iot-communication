package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class UploadParameterTest {

    @Test
    public void startUploadParameterTest() {

        byte[] expect = new byte[]{
                0x1D, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x09, 0x5F, 0x30, 0x42,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x41,
        };

        StartUploadParameter parameter = new StartUploadParameter();
        parameter.setFunctionCode(EFunctionCode.START_UPLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setErrorCode(new byte[]{0x00, 0x00});
        parameter.setId(0);
        parameter.setFileNameLength(9);
        parameter.setFileIdentifier("_");
        parameter.setBlockType(EFileBlockType.SDB);
        parameter.setBlockNumber(0);
        parameter.setDestinationFileSystem(EDestinationFileSystem.A);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = StartUploadParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_UPLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x00, 0x00}, parameter.getErrorCode());
        assertEquals(0, parameter.getId());
        assertEquals(9, parameter.getFileNameLength());
        assertEquals("_", parameter.getFileIdentifier());
        assertEquals(EFileBlockType.SDB, parameter.getBlockType());
        assertEquals(0, parameter.getBlockNumber());
        assertEquals(EDestinationFileSystem.A, parameter.getDestinationFileSystem());

    }

    @Test
    public void startUploadAckParameterTest() {

        byte[] expect = new byte[]{
                0x1D, 0x00, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x07,
                0x07, 0x30, 0x30, 0x30, 0x30, 0x32, 0x31, 0x36
        };

        StartUploadAckParameter parameter = new StartUploadAckParameter();
        parameter.setFunctionCode(EFunctionCode.START_UPLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setErrorCode(new byte[]{0x01, 0x00});
        parameter.setId(7);
        parameter.setBlockLength(7);
        parameter.setBlockLength(216);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = StartUploadAckParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_UPLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x01, 0x00}, parameter.getErrorCode());
        assertEquals(7, parameter.getId());
        assertEquals(7, parameter.getBlockLengthStringLength());
        assertEquals(216, parameter.getBlockLength());

    }

    @Test
    public void uploadParameterTest() {

        byte[] expect = new byte[]{
                0x1E, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x07,
        };

        UploadParameter parameter = new UploadParameter();
        parameter.setFunctionCode(EFunctionCode.UPLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setErrorCode(new byte[]{0x00, 0x00});
        parameter.setId(7);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = UploadParameter.fromBytes(expect);
        assertEquals(EFunctionCode.UPLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x00, 0x00}, parameter.getErrorCode());
        assertEquals(7, parameter.getId());

    }

    @Test
    public void uploadAckParameterTest() {

        byte[] expect = new byte[]{
                0x1E, 0x00
        };

        UpDownloadAckParameter parameter = new UpDownloadAckParameter();
        parameter.setFunctionCode(EFunctionCode.UPLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = UpDownloadAckParameter.fromBytes(expect);
        assertEquals(EFunctionCode.UPLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());

    }
}