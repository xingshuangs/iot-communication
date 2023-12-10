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
                (byte) 0xFA, 0x00, 0x01, 0x00,
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
        parameter.setErrorCode(new byte[]{0x01, 0x00});
        parameter.setId(0);
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
        assertArrayEquals(new byte[]{0x01, 0x00}, parameter.getErrorCode());
        assertEquals(0, parameter.getId());
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
                (byte) 0xFB, 0x00, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x09, 0x5F, 0x30, 0x41,
                0x30, 0x30, 0x30, 0x30, 0x31, 0x50,
        };

        DownloadParameter parameter = new DownloadParameter();
        parameter.setFunctionCode(EFunctionCode.DOWNLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        parameter.setErrorCode(new byte[]{0x01, 0x00});
        parameter.setId(0);
        parameter.setFileNameLength(9);
        parameter.setFileIdentifier("_");
        parameter.setBlockType(EFileBlockType.DB);
        parameter.setBlockNumber(1);
        parameter.setDestinationFileSystem(EDestinationFileSystem.P);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = DownloadParameter.fromBytes(expect);
        assertEquals(EFunctionCode.DOWNLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());
        assertArrayEquals(new byte[]{0x01, 0x00}, parameter.getErrorCode());
        assertEquals(0, parameter.getId());
        assertEquals(9, parameter.getFileNameLength());
        assertEquals("_", parameter.getFileIdentifier());
        assertEquals(EFileBlockType.DB, parameter.getBlockType());
        assertEquals(1, parameter.getBlockNumber());
        assertEquals(EDestinationFileSystem.P, parameter.getDestinationFileSystem());

    }

    @Test
    public void downloadAckParameterTest() {

        byte[] expect = new byte[]{
                (byte) 0xFA, 0x00
        };

        UploadAckParameter parameter = new UploadAckParameter();
        parameter.setFunctionCode(EFunctionCode.START_DOWNLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = UploadAckParameter.fromBytes(expect);
        assertEquals(EFunctionCode.START_DOWNLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());

    }
}