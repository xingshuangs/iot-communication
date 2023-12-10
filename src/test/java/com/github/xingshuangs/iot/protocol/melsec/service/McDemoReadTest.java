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
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.List;

/**
 * @author xingshuang
 */
public class McDemoReadTest {

    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.Q_L, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback(x -> System.out.printf("[%d] %s%n", x.length, HexUtil.toHexString(x)));

        // read boolean
        boolean booleanData = mcPLC.readBoolean("M100");

        // read boolean list
        List<Boolean> booleanList = mcPLC.readBoolean("M100", 10);

        // read one byte
        byte byteData = mcPLC.readByte("D100");

        // read multi byte
        byte[] bytes = mcPLC.readBytes("D100", 10);

        // read int16
        short int16Data = mcPLC.readInt16("D100");

        // read int16 list
        List<Integer> int16List = mcPLC.readUInt16("D100", "D108", "D130");

        // read uint16
        int uint16Data = mcPLC.readUInt16("D100");

        // read uint16 list
        List<Integer> uint16List = mcPLC.readUInt16("D100", "D108", "D130");

        // read int32
        int int32Data = mcPLC.readInt32("D100");

        // read int32 list
        List<Integer> int32List = mcPLC.readInt32("D100", "D108", "D130");

        // read uint32
        long uint32Data = mcPLC.readUInt32("D100");

        // read uint32 list
        List<Long> uint32List = mcPLC.readUInt32("D100", "D108", "D130");

        // read float32
        float float32Data = mcPLC.readFloat32("D100");

        // read float32 list
        List<Float> float32List = mcPLC.readFloat32("D100", "D108", "D130");

        // read float64
        double float64Data = mcPLC.readFloat64("D100");

        // read string
        String stringData = mcPLC.readString("D100", 6);

        // read multi address, only support word and dword
        McMultiAddressRead addressRead = new McMultiAddressRead();
        addressRead.addWordData("D100");
        addressRead.addWordData("D150");
        addressRead.addWordData("D130");
        addressRead.addWordData("D110");
        addressRead.addDWordData("D112");
        addressRead.addDWordData("D116");
        addressRead.addDWordData("D126");
        List<McDeviceContent> contentList = mcPLC.readMultiAddress(addressRead);

        mcPLC.close();
    }
}
