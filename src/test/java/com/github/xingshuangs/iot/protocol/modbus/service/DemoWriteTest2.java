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

import java.util.Arrays;
import java.util.List;

/**
 * @author xingshuang
 */
public class DemoWriteTest2 {

    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // optional
        plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // single write coil
        plc.writeCoil(2, 0, true);

        // multiple write coil
        List<Boolean> booleans = Arrays.asList(true, false, true, false);
        plc.writeCoil(2, 0, booleans);

        // single write hold register
        plc.writeHoldRegister(2, 0, 33);
        // multiple write hold register
        plc.writeHoldRegister(2, 3, new byte[]{(byte) 0x11, (byte) 0x12});
        // multiple write hold register
        List<Integer> integers = Arrays.asList(11, 12, 13, 14);
        plc.writeHoldRegister(2, 3, integers);

        // hold register write int16
        plc.writeInt16(3, 2, (short) 10, true);

        // hold register write uint16
        plc.writeUInt16(3, 2, 20, false);

        // hold register write int32
        plc.writeInt32(4, 2, 32, EByteBuffFormat.AB_CD);

        // hold register write uint32
        plc.writeUInt32(4, 2, 32L, EByteBuffFormat.AB_CD);

        // hold register write float32
        plc.writeFloat32(4, 2, 12.12f, EByteBuffFormat.AB_CD);

        // hold register write float64
        plc.writeFloat64(4, 2, 33.21, EByteBuffFormat.AB_CD);

        // hold register write String
        plc.writeString(5, 2, "1234");

        plc.close();
    }
}
