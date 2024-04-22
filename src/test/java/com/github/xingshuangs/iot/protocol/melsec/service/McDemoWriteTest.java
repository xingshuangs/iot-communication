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


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.HexUtil;

/**
 * @author xingshuang
 */
public class McDemoWriteTest {

    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.Q_L, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // write one boolean
        mcPLC.writeBoolean("M100", true);

        // write multi boolean
        mcPLC.writeBoolean("M100", true, false, true);

        // write one byte
        mcPLC.writeByte("D100", (byte) 0x01);

        // write multi byte
        mcPLC.writeBytes("D100", new byte[]{0x01, 0x02, 0x03});

        // write one int16
        mcPLC.writeInt16("D100", (short) 16);

        // write multi int16
        mcPLC.writeInt16("D100", (short) 16, (short) 17, (short) 118);

        // write one uint16
        mcPLC.writeUInt16("D100", 16);

        // write multi uint16
        mcPLC.writeUInt16("D100", 16, 17, 18);

        // write one int32
        mcPLC.writeInt32("D100", 55);

        // write multi int32
        mcPLC.writeInt32("D100", 55, 66, 77);

        // write one uint32
        mcPLC.writeUInt32("D100", 55);

        // write multi uint32
        mcPLC.writeUInt32("D100", 55L, 66L, 77L);

        // write one float32
        mcPLC.writeFloat32("D100", 0.123f);

        // write multi float32
        mcPLC.writeFloat32("D100", 0.123f, 145.56f, 88.12f);

        // write one float64
        mcPLC.writeFloat64("D100", 0.123);

        // write multi float64
        mcPLC.writeFloat64("D100", 0.123, 145.56, 88.12);

        // write string
        mcPLC.writeString("D100", "1234567890");

        // multi write, only support int16 uint16 int32 uint32 float32
        McMultiAddressWrite addressWrite = new McMultiAddressWrite();
        addressWrite.addInt16("D100", 110);
        addressWrite.addUInt16("D101", 110);
        addressWrite.addInt32("D102", 110);
        addressWrite.addUInt32("D104", 110);
        addressWrite.addFloat32("D106", 110);
        mcPLC.writeMultiAddress(addressWrite);

        mcPLC.close();
    }
}
