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

package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.List;

/**
 * @author xingshuang
 */
public class DemoReadTest2 {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // optional
        plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // read coil
        List<Boolean> readCoil = plc.readCoil(2, 0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(2, 0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(2, 0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(2, 0, 2);

        // hold register read Boolean
        boolean readBoolean = plc.readBoolean(3, 2, 1);

        // hold register read Int16
        short readInt16 = plc.readInt16(3, 2, true);

        // hold register read UInt16
        int readUInt16 = plc.readUInt16(3, 2, false);

        // hold register read Int32
        int readInt32 = plc.readInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Int32
        long readUInt32 = plc.readUInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float32
        float readFloat32 = plc.readFloat32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float64
        double readFloat64 = plc.readFloat64(4, 2, EByteBuffFormat.AB_CD);

        // hold register read String
        String readString = plc.readString(5, 2, 4);

        plc.close();
    }
}
