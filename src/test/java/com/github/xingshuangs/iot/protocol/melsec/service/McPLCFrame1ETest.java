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

package com.github.xingshuangs.iot.protocol.melsec.service;

import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.xingshuangs.iot.common.constant.GeneralConst.LOCALHOST;
import static org.junit.Assert.*;

@Slf4j
@Ignore
public class McPLCFrame1ETest {

    private final McPLC mcPLC = new McPLC(EMcSeries.A, LOCALHOST, 6000);

    @Before
    public void before() {
        this.mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
    }

    @Test
    public void readWriteDeviceBatchInWord() {
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("D110", 2, expect);
        this.mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("D110", 2);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInWord(address);
        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readWriteDeviceBatchInWordOutRange() {
        McDeviceContent reqContent = McDeviceContent.createBy("D110", 1924, new byte[3848]);
        this.mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("D110", 1924);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInWord(address);
        assertEquals(3848, ackContent.getData().length);
    }

    @Test
    public void readWriteDeviceBatchInBit() {
        byte[] expect = new byte[]{0x11, 0x00, 0x01, 0x10};
        McDeviceContent reqContent = McDeviceContent.createBy("M110", 8, expect);
        this.mcPLC.writeDeviceBatchInBit(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M110", 8);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInBit(address);
        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readWriteDeviceBatchInBitOutRange() {
        McDeviceContent reqContent = McDeviceContent.createBy("M110", 7170, new byte[3585]);
        this.mcPLC.writeDeviceBatchInBit(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M110", 7170);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInBit(address);
        assertEquals(3585, ackContent.getData().length);
    }

    @Test()
    public void readWriteBooleanOutRange() {
        List<Boolean> data = new ArrayList<>();
        for (int i = 0; i < 7170; i++) {
            data.add(false);
        }
        this.mcPLC.writeBoolean("M110", data);
        // 超范围读取
        List<Boolean> booleanList = this.mcPLC.readBoolean("M110", 7170);
        assertEquals(7170, booleanList.size());
    }

    @Test
    public void readWriteBytesOutRange() {
        // 超范围写入
        this.mcPLC.writeBytes("D110", new byte[3848]);
        // 超范围读取
        byte[] d110s = this.mcPLC.readBytes("D110", 1924);
        assertEquals(1924, d110s.length);

    }

    @Test
    public void readWriteBoolean() {
        this.mcPLC.writeBoolean("M110", true);
        boolean m110 = this.mcPLC.readBoolean("M110");
        assertTrue(m110);

        this.mcPLC.writeBoolean("M110", false);
        m110 = this.mcPLC.readBoolean("M110");
        assertFalse(m110);

        this.mcPLC.writeBoolean("M120", true, true, true);
        List<Boolean> booleanList = this.mcPLC.readBoolean("M120", 3);
        assertEquals(3, booleanList.size());
        booleanList.forEach(Assert::assertTrue);

        this.mcPLC.writeBoolean("M120", false, false, false);
        booleanList = this.mcPLC.readBoolean("M120", 3);
        assertEquals(3, booleanList.size());
        booleanList.forEach(Assert::assertFalse);
    }

    @Test
    public void readWriteInt16() {
        this.mcPLC.writeInt16("D110", (short) 99);
        short shortData = this.mcPLC.readInt16("D110");
        assertEquals(99, shortData);
    }

    @Test
    public void readWriteUInt16() {
        this.mcPLC.writeUInt16("D110", 99);
        int shortData = this.mcPLC.readUInt16("D110");
        assertEquals(99, shortData);
    }

    @Test(expected = McCommException.class)
    public void readWriteInt16_1() {
        short int16 = this.mcPLC.readInt16("113");
        System.out.println(int16);
    }

    @Test
    public void readWriteInt32() {
        this.mcPLC.writeInt32("D114", 799864);
        int data = this.mcPLC.readInt32("D114");
        assertEquals(799864, data);
    }

    @Test
    public void readWriteUInt32() {
        this.mcPLC.writeUInt32("D114", 799864);
        long data = this.mcPLC.readUInt32("D114");
        assertEquals(799864, data);
    }

    @Test
    public void readWriteFloat32() {
        this.mcPLC.writeFloat32("D114", 33.66f);
        float data = this.mcPLC.readFloat32("D114");
        assertEquals(33.66f, data, 0.001);
    }

    @Test
    public void readAndWriteData() {
        this.mcPLC.writeBoolean("M100", true);
        boolean m100 = this.mcPLC.readBoolean("M100");
        assertTrue(m100);

        this.mcPLC.writeByte("D100", (byte) 0x01);
        byte d100 = this.mcPLC.readByte("D100");
        assertEquals(0x01, d100);

        this.mcPLC.writeInt16("D100", (short) 66);
        short int16 = this.mcPLC.readInt16("D100");
        assertEquals(66, int16);

        this.mcPLC.writeUInt16("D100", 77);
        int uint16 = this.mcPLC.readUInt16("D100");
        assertEquals(77, uint16);

        this.mcPLC.writeInt32("D100", 88);
        int int32 = this.mcPLC.readInt32("D100");
        assertEquals(88, int32);

        this.mcPLC.writeUInt32("D100", 99);
        long uint32 = this.mcPLC.readUInt32("D100");
        assertEquals(99, uint32);

        this.mcPLC.writeFloat32("D100", 99.33f);
        float float32 = this.mcPLC.readFloat32("D100");
        assertEquals(99.33f, float32, 0.001);

        this.mcPLC.writeFloat64("D100", 151.33f);
        double float64 = this.mcPLC.readFloat64("D100");
        assertEquals(151.33f, float64, 0.001);

        this.mcPLC.writeString("D100", "123456");
        String string = this.mcPLC.readString("D100", 6);
        assertEquals("123456", string);
    }
}