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
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import org.junit.Test;

import static org.junit.Assert.*;


public class ReadWriteParameterTest {

    @Test
    public void byteArrayLength() {
        RequestItem requestItem = new RequestItem();
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.getRequestItems().add(requestItem);
        assertEquals(14, readWriteParameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        RequestItem requestItem = new RequestItem();
        requestItem.setSpecificationType((byte) 0x12);
        requestItem.setLengthOfFollowing((byte) 0x10);
        requestItem.setSyntaxId(ESyntaxID.S7ANY);
        requestItem.setVariableType(EParamVariableType.BYTE);
        requestItem.setCount(7);
        requestItem.setDbNumber(7);
        requestItem.setArea(EArea.DATA_BLOCKS);
        requestItem.setByteAddress(3);
        requestItem.setBitAddress(1);
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.setFunctionCode(EFunctionCode.READ_VARIABLE);
        readWriteParameter.setItemCount((byte) 0x01);
        readWriteParameter.getRequestItems().add(requestItem);
        byte[] actual = readWriteParameter.toByteArray();
        byte[] expect = new byte[]{(byte) 0x04, (byte) 0x01, (byte) 0x12, (byte) 0x10, (byte) 0x10, (byte) 0x02, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x07, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x19};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{
                // parameter：功能 + 个数
                (byte) 0x05, (byte) 0x02,
                // request item
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x02,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x01,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x19,
                // request item-------------------------------
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x04,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x03,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x08
        };

        ReadWriteParameter parameter = ReadWriteParameter.fromBytes(data);
        RequestItem r0 = (RequestItem) parameter.getRequestItems().get(0);
        RequestItem r1 = (RequestItem) parameter.getRequestItems().get(1);
        assertEquals(EParamVariableType.BYTE, r0.getVariableType());
        assertEquals(1, r0.getBitAddress());
        assertEquals(3, r0.getByteAddress());
        assertEquals(1, r0.getDbNumber());
        assertEquals(EParamVariableType.WORD, r1.getVariableType());
        assertEquals(0, r1.getBitAddress());
        assertEquals(1, r1.getByteAddress());
        assertEquals(3, r1.getDbNumber());
    }
}