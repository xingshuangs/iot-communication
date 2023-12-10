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

import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import org.junit.Test;

import static org.junit.Assert.*;


public class RequestItemTest {

    @Test
    public void byteArrayLength() {
        RequestItem requestItem = new RequestItem();
        assertEquals(12, requestItem.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        RequestItem requestItem = new RequestItem();
        requestItem.setSpecificationType((byte)0x12);
        requestItem.setLengthOfFollowing((byte)0x10);
        requestItem.setSyntaxId(ESyntaxID.S7ANY);
        requestItem.setVariableType(EParamVariableType.BYTE);
        requestItem.setCount(7);
        requestItem.setDbNumber(7);
        requestItem.setArea(EArea.DATA_BLOCKS);
        requestItem.setByteAddress(3);
        requestItem.setBitAddress(1);
        byte[] actual = requestItem.toByteArray();
        byte[] expect = new byte[]{(byte) 0x12, (byte) 0x10, (byte) 0x10, (byte) 0x02, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x07, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x19};
        assertArrayEquals(expect, actual);
    }
}