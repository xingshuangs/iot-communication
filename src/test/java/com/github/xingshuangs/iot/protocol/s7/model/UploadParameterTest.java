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

        UploadAckParameter parameter = new UploadAckParameter();
        parameter.setFunctionCode(EFunctionCode.UPLOAD);
        parameter.setMoreDataFollowing(false);
        parameter.setErrorStatus(false);
        byte[] actual = parameter.toByteArray();
        assertArrayEquals(expect, actual);

        parameter = UploadAckParameter.fromBytes(expect);
        assertEquals(EFunctionCode.UPLOAD, parameter.getFunctionCode());
        assertFalse(parameter.isMoreDataFollowing());
        assertFalse(parameter.isErrorStatus());

    }
}